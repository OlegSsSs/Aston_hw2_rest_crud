package repository;

import entity.Order;
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
class OrderRepositoryTest {
    @Mock
    private ConnectionPool connectionPool;
    private OrderRepository orderRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private OrderRepository spyOrderRepository;
    private Order supposedOrder;
    private static final String SQL_INSERT_ORDERS = "INSERT INTO mydatabase.orders (name) VALUES (?)";
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.orders WHERE id = ?";

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository(connectionPool);
        spyOrderRepository = spy(orderRepository);

        supposedOrder = new Order();
        supposedOrder.setId(1L);
        supposedOrder.setName("test name order");
    }
    @Test
    void testCreateOrder() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        orderRepository.saveOrder(supposedOrder);

        verify(connection).prepareStatement(SQL_INSERT_ORDERS, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setString(1, supposedOrder.getName());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetOrder() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyOrderRepository.getOrderById(1L)).thenReturn(supposedOrder);

        Order actualOrder = spyOrderRepository.getOrderById(1L);

        assertNotNull(actualOrder);
        assertEquals(supposedOrder, actualOrder);
        verify(connection).prepareStatement(SQL_SELECT_GET_BY_ID);
    }

    @Test
    void testUpdateOrder() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Order updateOrder = new Order();
        updateOrder.setId(1L);
        updateOrder.setName("test update name order");

        Order order = orderRepository.updateOrder(updateOrder);

        assertNotNull(order);
        assertEquals(updateOrder, order);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setString(1, updateOrder.getName());
    }
}