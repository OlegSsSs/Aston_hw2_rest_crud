package repository;

import entity.Product;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    ConnectionPool connectionPool = new ConnectionPool();

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mydatabase.products");
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
                    .prepareStatement("SELECT * FROM mydatabase.products WHERE id =?")) {
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

    public Product saveProduct(Product product) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                   .prepareStatement("INSERT INTO mydatabase.products (name, user_id) VALUES (?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setLong(2, product.getUserId());
            statement.executeUpdate();

            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    product.setId(generatedKey.getLong(1));
                } else {
                    throw new RuntimeException("Failed to get a generated key");
                }
            }
            connection.commit();
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product updateProduct(Product product) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                  .prepareStatement("UPDATE mydatabase.products SET name =?, user_id =? WHERE id =?")) {
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
                   .prepareStatement("DELETE FROM mydatabase.products WHERE id =?")) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
