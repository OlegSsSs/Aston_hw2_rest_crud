package repository;

import entity.Order;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    ConnectionPool connectionPool = new ConnectionPool();

    public List<Order> getAllOrder() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mydatabase.orders");
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getLong(1));
                order.setName(resultSet.getString(2));

                orders.add(order);

            }
            connection.commit();
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order getOrderById(long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("SELECT * FROM mydatabase.orders WHERE id =?")) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Order order = new Order();

            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
                order.setName(resultSet.getString(2));
            }
            connection.commit();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order saveOrder(Order order) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO mydatabase.orders (name) VALUES (?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, order.getName());
            statement.executeUpdate();

            try (ResultSet generatedKey = statement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    order.setId(generatedKey.getLong(1));
                } else {
                    throw new RuntimeException("Failed to get a generated key");
                }
            }
            connection.commit();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order saveOrder(Order order, List<Long> productsId) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statementOrder = connection.prepareStatement(
                    "INSERT INTO mydatabase.orders (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement statementOrderProduct = connection.prepareStatement(
                    "INSERT INTO mydatabase.orders_products (order_id, product_id) VALUES (?,?)");
            statementOrder.setString(1, order.getName());
            statementOrder.executeUpdate();

            ResultSet generatedKey = statementOrderProduct.getGeneratedKeys();
            if (generatedKey.next()) {
                order.setId(generatedKey.getLong(1));

                for (Long productId : productsId) {
                    statementOrderProduct.setLong(1, order.getId());
                    statementOrderProduct.setLong(2, productId);
                    statementOrderProduct.executeUpdate();
                }
            }
            connection.commit();
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Order updateOrder(Order order) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("UPDATE mydatabase.orders SET mydatabase.orders.name =?, user_id =? WHERE id =?")) {
            statement.setString(1, order.getName());
            statement.setLong(2, order.getId());
            statement.executeUpdate();

            connection.commit();

            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteOrder(long id) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement("DELETE FROM mydatabase.orders WHERE id =?")) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
