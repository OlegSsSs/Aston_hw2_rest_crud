package service;

import dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProduct();

    ProductDto getProductById(Long id);

    ProductDto saveProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(Long id);
}