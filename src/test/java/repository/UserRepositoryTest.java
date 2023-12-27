package repository;

import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {
    @Mock
    private ConnectionPool connectionPool;
    private UserRepository userRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private UserRepository spyUserRepository;
    private User supposedUser;
    private static final String SQL_INSERT_USERS = "INSERT INTO mydatabase.users (name, email) VALUES (?, ?)";
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.users WHERE id = ?";

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(connectionPool);
        spyUserRepository = spy(userRepository);

        supposedUser = new User();
        supposedUser.setId(1L);
        supposedUser.setName("test name user");
        supposedUser.setEmail("test email user");
    }

    @Test
    void testCreateUser() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        userRepository.saveUser(supposedUser);

        verify(connection).prepareStatement(SQL_INSERT_USERS, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setString(1, supposedUser.getName());
        verify(preparedStatement).setString(2, supposedUser.getEmail());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetUser() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyUserRepository.getUserById(1L)).thenReturn(supposedUser);

        User actualUser = spyUserRepository.getUserById(1L);

        assertNotNull(actualUser);
        assertEquals(supposedUser, actualUser);
        verify(connection).prepareStatement(SQL_SELECT_GET_BY_ID);
    }

    @Test
    void testUpdateUser() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        User updateUser = new User();
        updateUser.setId(1L);
        updateUser.setName("test update name user");

        User user = userRepository.updateUser(updateUser);

        assertNotNull(user);
        assertEquals(updateUser, user);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setString(1, updateUser.getName());
    }
}