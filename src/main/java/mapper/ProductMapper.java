package mapper;

import dto.ProductDto;
import entity.Product;

public class ProductMapper implements Mapper<Product, ProductDto> {
    @Override
    public Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setUserId(dto.getUserId());

        return product;
    }

    @Override
    public ProductDto toDto(Product user) {
        return ProductDto.builder()
                .id(user.getId())
                .name(user.getName())
                .userId(user.getUserId())
                .build();
    }
}
