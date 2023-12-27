package mapper;

import dto.OrderDto;
import entity.Order;

public class OrderMapper implements Mapper<Order, OrderDto> {
    @Override
    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setName(dto.getName());

        return order;
    }

    @Override
    public OrderDto toDto(Order user) {
        return OrderDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}