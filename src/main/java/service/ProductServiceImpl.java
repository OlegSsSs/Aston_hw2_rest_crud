package service;

import dto.ProductDto;
import entity.Product;
import mapper.Mapper;
import mapper.ProductMapper;
import repository.ProductRepository;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Mapper<Product, ProductDto> mapper;

    public ProductServiceImpl(ProductRepository productRepository, Mapper<Product, ProductDto> mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    public ProductServiceImpl() {
        this.productRepository = new ProductRepository();
        this.mapper = new ProductMapper();
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> productList = productRepository.getAllProducts();
        return productList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.getProductById(id);
        return mapper.toDto(product);
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        Product savedProduct = productRepository.saveProduct(product);
        return mapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        Product updatedProducts = productRepository.updateProduct(product);
        return mapper.toDto(updatedProducts);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.deleteProduct(id);
    }
}