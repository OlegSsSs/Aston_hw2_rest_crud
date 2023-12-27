package dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderProductDto {
    private OrderDto orderDto;
    private List<Long> productId;

    @Override
    public int hashCode() {
        return Objects.hash(orderDto, productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()!= obj.getClass()) return false;
        return Objects.equals(orderDto, ((OrderProductDto) obj).orderDto) &&
                Objects.equals(productId, ((OrderProductDto) obj).productId);
    }
}