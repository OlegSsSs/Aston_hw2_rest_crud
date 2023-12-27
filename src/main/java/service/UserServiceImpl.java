package service;

import mapper.UserMapper;
import repository.UserRepository;
import dto.UserDto;
import entity.User;
import mapper.Mapper;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mapper<User, UserDto> mapper;

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
        this.mapper = new UserMapper();
    }

    public UserServiceImpl(UserRepository userRepository, Mapper<User, UserDto> mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        return userList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.getUserById(id);
        return mapper.toDto(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        User savedUser = userRepository.saveUser(user);
        return mapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        User updatedUser = userRepository.updateUser(user);
        return mapper.toDto(updatedUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }
}