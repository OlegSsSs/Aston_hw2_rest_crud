package service;

import dto.ProductDto;
import entity.Product;
import mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.ProductRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private Mapper<Product, ProductDto> mapper;
    private ProductServiceImpl productService;
    private ProductDto supposedProductDto;
    private Product supposedProduct;
    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, mapper);

        supposedProductDto = ProductDto.builder()
                .id(1L)
                .name("test name product")
                .userId(1L)
                .build();

        supposedProduct = new Product();
        supposedProduct.setId(1L);
        supposedProduct.setName("test name product");
        supposedProduct.setUserId(1L);
    }

    @Test
    void testGetProduct() {
        when(productRepository.saveProduct(any(Product.class))).thenReturn(supposedProduct);
        when(mapper.toEntity(any(ProductDto.class))).thenReturn(supposedProduct);

        productService.saveProduct(supposedProductDto);

        verify(mapper).toEntity(supposedProductDto);
        verify(productRepository).saveProduct(supposedProduct);
        verify(mapper).toDto(supposedProduct);
    }


    @Test
    void testSaveProduct() {
        when(productRepository.saveProduct(any(Product.class))).thenReturn(supposedProduct);
        when(mapper.toEntity(any(ProductDto.class))).thenReturn(supposedProduct);

        productService.saveProduct(supposedProductDto);

        verify(mapper).toEntity(supposedProductDto);
        verify(productRepository).saveProduct(supposedProduct);
        verify(mapper).toDto(supposedProduct);
    }

    @Test
    void testUpdateProduct() {
        when(mapper.toEntity(any(ProductDto.class))).thenReturn(supposedProduct);
        when(productRepository.updateProduct(any(Product.class))).thenReturn(supposedProduct);
        when(mapper.toDto(any(Product.class))).thenReturn(supposedProductDto);

        ProductDto actualProductDto = productService.updateProduct(supposedProductDto);

        verify(mapper).toEntity(supposedProductDto);
        verify(productRepository).updateProduct(supposedProduct);
        verify(mapper).toDto(supposedProduct);
        assertEquals(supposedProductDto, actualProductDto);
    }
}