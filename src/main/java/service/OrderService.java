package service;

import dto.OrderDto;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrder();

    OrderDto getOrderById(Long id);

    OrderDto saveOrder(OrderDto orderDto);

    OrderDto saveOrder(OrderDto orderDto, List<Long> productsId);

    OrderDto updateOrder(OrderDto orderDto);

    boolean deleteOrder(Long id);
}