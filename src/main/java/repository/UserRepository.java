package repository;

import entity.User;
import lombok.SneakyThrows;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final ConnectionPool connectionPool;
    private static final String SQL_SELECT_GET_ALL = "SELECT * FROM mydatabase.users LIMIT 10 OFFSET 0";
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.users WHERE id = ?";
    private static final String SQL_INSERT_USERS = "INSERT INTO mydatabase.users (name, email) VALUES (?, ?)";
    private static final String SQL_UPDATE_USERS = "UPDATE mydatabase.users SET name = ?, email = ? WHERE id = ?";
    private static final String SQL_DELETE_USERS = "DELETE FROM mydatabase.users WHERE id = ?";

    public UserRepository() {
        connectionPool = new ConnectionPool();
    }

    public UserRepository(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @SneakyThrows
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));

                users.add(user);

            }
            connection.commit();
            return users;
        } catch (SQLException e) {
            connectionPool.getConnection().rollback();
            throw new RuntimeException("A transaction has been rolled back");
        }
    }

    @SneakyThrows
    public User getUserById(Long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(SQL_SELECT_GET_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            User user = new User();

            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setEmail(resultSet.getString(3));
            }
            connection.commit();
            return user;
        } catch (SQLException e) {
            connectionPool.getConnection().rollback();
            throw new RuntimeException("A transaction has been rolled back");
        }
    }

    @SneakyThrows
    public User saveUser(User user) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_USERS,
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.executeUpdate();

            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    user.setId(generatedKey.getLong(1));
                } else {
                    throw new RuntimeException("Failed to get a generated key");
                }
            }
            connection.commit();
            return user;
        } catch (SQLException e) {
            connectionPool.getConnection().rollback();
            throw new RuntimeException("A transaction has been rolled back");
        }
    }

    @SneakyThrows
    public User updateUser(User user) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(SQL_UPDATE_USERS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());
            statement.executeUpdate();

            connection.commit();

            return user;
        } catch (SQLException e) {
            connectionPool.getConnection().rollback();
            throw new RuntimeException("A transaction has been rolled back");
        }
    }

    public boolean deleteUser(Long id) {
        try (Connection connection = connectionPool.getConnection();

             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_USERS)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}