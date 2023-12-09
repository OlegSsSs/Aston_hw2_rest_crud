package dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class OrderProductDto {
    private OrderDto orderDto;
    private List<Long> productId;
}
