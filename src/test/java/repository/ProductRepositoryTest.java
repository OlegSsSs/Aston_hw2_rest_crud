package repository;

import entity.Product;
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
class ProductRepositoryTest {
    @Mock
    private ConnectionPool connectionPool;
    private ProductRepository productRepository;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    private ProductRepository spyProductRepository;
    private Product supposedProduct;
    private static final String SQL_SELECT_GET_BY_ID = "SELECT * FROM mydatabase.products WHERE id =?";
    private static final String SQL_INSERT_PRODUCTS = "INSERT INTO mydatabase.products (name, user_id) VALUES (?, ?)";

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository(connectionPool);
        spyProductRepository = spy(productRepository);
        supposedProduct = new Product();
        supposedProduct.setId(1L);
        supposedProduct.setName("test name product");
        supposedProduct.setUserId(1L);
    }

    @Test
    void testCreateProduct() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(preparedStatement.getGeneratedKeys().next()).thenReturn(true);
        when(preparedStatement.getGeneratedKeys().getLong(1)).thenReturn(1L);
        when(connection.prepareStatement(anyString(), eq(RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);

        productRepository.saveProduct(supposedProduct);

        verify(connection).prepareStatement(SQL_INSERT_PRODUCTS, RETURN_GENERATED_KEYS);
        verify(preparedStatement).setString(1, supposedProduct.getName());
        verify(preparedStatement).setLong(2, supposedProduct.getUserId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetProduct() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(spyProductRepository.getProductById(1L)).thenReturn(supposedProduct);

        Product actualProduct = spyProductRepository.getProductById(1L);

        assertNotNull(actualProduct);
        assertEquals(supposedProduct, actualProduct);
        verify(connection).prepareStatement(SQL_SELECT_GET_BY_ID);
    }

    @Test
    void testUpdateProduct() throws SQLException {
        when(connectionPool.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Product updateProduct = new Product();
        updateProduct.setId(1L);
        updateProduct.setName("test update name product");
        updateProduct.setUserId(1L);

        Product product = productRepository.updateProduct(updateProduct);

        assertNotNull(product);
        assertEquals(updateProduct, product);
        verify(preparedStatement).executeUpdate();
        verify(preparedStatement).setString(1, updateProduct.getName());
        verify(preparedStatement).setLong(2, updateProduct.getUserId());
    }
}