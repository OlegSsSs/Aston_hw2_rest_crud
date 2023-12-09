package service;

import dto.ProductDto;
import entity.Product;
import mapper.Mapper;
import mapper.ProductMapper;
import repository.ProductRepository;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final Mapper<Product, ProductDto> mapper = new ProductMapper();

    public ProductServiceImpl() {
        this.productRepository = new ProductRepository();
    }

    @Override
    public List<ProductDto> getAllProduct() {
        List<Product> productList = productRepository.getAllProducts();
        return productList.stream().map(mapper::toDo).toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.getProductById(id);
        return mapper.toDo(product);
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        Product product = mapper.toEntity(productDto);
        Product savedProduct = productRepository.saveProduct(product);
        return mapper.toDo(savedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.updateProduct(mapper.toEntity(productDto));
        return mapper.toDo(product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.deleteProduct(id);
    }
}
