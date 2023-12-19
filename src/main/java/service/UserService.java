package service;

import dto.UserDto;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    
    UserDto getUserById(Long id);
    
    UserDto saveUser(UserDto userDto);
    
    UserDto updateUser(UserDto userDto);
    
    boolean deleteUser(Long id);
}
