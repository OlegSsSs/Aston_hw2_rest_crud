package service;

import dto.UserDto;
import entity.User;
import mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Mapper<User, UserDto> mapper;
    private UserService userService;
    private UserDto supposedUserDto;
    private User supposedUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, mapper);

        supposedUserDto = UserDto.builder()
                .id(1L)
                .name("test name user")
                .email("test email user")
                .build();

        supposedUser = new User();
        supposedUser.setId(1L);
        supposedUser.setName("test user");
        supposedUser.setEmail("test email user");
    }

    @Test
    void testGetUsers() {
        when(userRepository.getUserById(anyLong())).thenReturn(supposedUser);
        when(userService.getUserById(anyLong())).thenReturn(supposedUserDto);

        UserDto actualUserDto = userService.getUserById(1L);

        verify(mapper).toDto(supposedUser);
        assertEquals(supposedUserDto, actualUserDto);
    }
    @Test
    void testSaveUser() {
        when(userRepository.saveUser(any(User.class))).thenReturn(supposedUser);
        when(mapper.toEntity(any(UserDto.class))).thenReturn(supposedUser);

        userService.saveUser(supposedUserDto);

        verify(mapper).toEntity(supposedUserDto);
        verify(userRepository).saveUser(supposedUser);
        verify(mapper).toDto(supposedUser);
    }

    @Test
    void testUpdateUser() {
        when(mapper.toEntity(any(UserDto.class))).thenReturn(supposedUser);
        when(userRepository.updateUser(any(User.class))).thenReturn(supposedUser);
        when(mapper.toDto(any(User.class))).thenReturn(supposedUserDto);

        UserDto actualUserDto = userService.updateUser(supposedUserDto);

        verify(mapper).toEntity(supposedUserDto);
        verify(userRepository).updateUser(supposedUser);
        verify(mapper).toDto(supposedUser);
        assertEquals(supposedUserDto, actualUserDto);
    }
}