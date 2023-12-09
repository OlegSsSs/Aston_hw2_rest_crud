package service;

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
        return userList.stream().map(mapper::toDo).toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.getUserById(id);
        return mapper.toDo(user);
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        User user = mapper.toEntity(userDto);
        User savedUser = userRepository.saveUser(user);
        return mapper.toDo(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.updateUser(mapper.toEntity(userDto));
        return mapper.toDo(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.deleteUser(id);
    }
}
