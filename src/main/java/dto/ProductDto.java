package dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ProductDto {
    private Long id;
    private String name;
    private Long userId;
}
