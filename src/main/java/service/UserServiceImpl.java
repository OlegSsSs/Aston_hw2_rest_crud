package service;

import lombok.SneakyThrows;
import repository.UserRepository;
import dto.UserDto;
import entity.User;
import mapper.Mapper;
import mapper.UserMapper;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Mapper<User, UserDto> mapper = new UserMapper();

    public UserServiceImpl() {
        this.userRepository = new UserRepository();
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> userList = userRepository.getAllUsers();
        return userList.stream().map(mapper::toDto).toList();
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
