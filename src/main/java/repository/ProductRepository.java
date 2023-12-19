package repository;

import entity.Product;
import lombok.SneakyThrows;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductRepository {
    private static final String SQL_SELECT_GET_ALL = "SELECT * FROM mydatabase.products LIMIT 12 OFFSET 0";
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.products WHERE id =?";
    private static final String SQL_INSERT_PRODUCTS = "INSERT INTO mydatabase.products (name, user_id) VALUES (?,?)";
    private static final String SQL_UPDATE_PRODUCTS = "UPDATE mydatabase.products SET name =?, user_id =? WHERE id =?";
    private static final String SQL_DELETE_PRODUCTS = "DELETE FROM mydatabase.products WHERE id =?";
    ConnectionPool connectionPool = new ConnectionPool();

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getLong(1));
                product.setName(resultSet.getString(2));
                product.setUserId(resultSet.getLong(3));

                products.add(product);

            }
            connection.commit();
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProductById(long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                    .prepareStatement(SQL_SELECT_GET_BY_ID)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Product product = new Product();

            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
                product.setName(resultSet.getString(2));
                product.setUserId(resultSet.getLong(3));
            }
            connection.commit();
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public Product saveProduct(Product product) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PRODUCTS, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setLong(2, product.getUserId());
            statement.executeUpdate();

            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    long productId = generatedKey.getLong(1);
                    product.setId(productId);
                } else {
                    throw new RuntimeException("Failed to get a generated key");
                }
            }
            connection.commit();
            return product;
        } catch (SQLException e) {
            if (connectionPool.getConnection() != null) {
                try {
                    connectionPool.getConnection().rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Failed to rollback transaction", ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(Product product) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                  .prepareStatement(SQL_UPDATE_PRODUCTS)) {
            statement.setString(1, product.getName());
            statement.setLong(2, product.getUserId());
            statement.setLong(3, product.getId());
            statement.executeUpdate();

            connection.commit();

            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteProduct(long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                   .prepareStatement(SQL_DELETE_PRODUCTS)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
