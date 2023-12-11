package repository;

import entity.Order;
import lombok.SneakyThrows;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private static final String SQL_SELECT_GET_ALL = "SELECT * FROM mydatabase.orders LIMIT 10 OFFSET 0";
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.orders WHERE id = ?";
    private static final String SQL_INSERT_ORDERS = "INSERT INTO mydatabase.orders (name) VALUES (?)";
    private static final String SQL_INSERT_ORDER_PRODUCTS = "INSERT INTO mydatabase.orders_products (order_id, product_id) VALUES (?, ?)";
    private static final String SQL_UPDATE_ORDERS = "UPDATE mydatabase.orders SET mydatabase.orders.name = ? WHERE id = ?";
    private static final String SQL_DELETE_ORDERS = "DELETE FROM mydatabase.orders WHERE id = ?";

    ConnectionPool connectionPool = new ConnectionPool();

    public List<Order> getAllOrder() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_ALL)) {
            ResultSet resultSet = statement.executeQuery();
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
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_GET_BY_ID)) {
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
    @SneakyThrows
    public Order saveOrder(Order order) {
        try (Connection connection = connectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_ORDERS, Statement.RETURN_GENERATED_KEYS)) {
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
            if (connectionPool.getConnection()!= null) {
                try {
                    connectionPool.getConnection().rollback();
                } catch (SQLException ex) {
                    throw new RuntimeException("Failed to rollback transaction", ex);
                }
            }
            throw new RuntimeException(e);
        }
    }

    public Order saveOrder(Order order, List<Long> productsId) {
        try {
            Connection connection = connectionPool.getConnection();
            PreparedStatement statementOrder = connection.prepareStatement(SQL_INSERT_ORDERS, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement statementOrderProduct = connection.prepareStatement(SQL_INSERT_ORDER_PRODUCTS, Statement.RETURN_GENERATED_KEYS);
            statementOrder.setString(1, order.getName());
            statementOrder.executeUpdate();

            ResultSet generatedKey = statementOrderProduct.getGeneratedKeys();
            if (generatedKey.next()) {
                long orderId = generatedKey.getLong(1);
                order.setId(orderId);

                for (Long productId : productsId) {
                    statementOrderProduct.setLong(1, orderId);
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
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_ORDERS)) {
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
                     .prepareStatement(SQL_DELETE_ORDERS)) {
            statement.setLong(1, id);
            int result = statement.executeUpdate();
            connection.commit();
            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
