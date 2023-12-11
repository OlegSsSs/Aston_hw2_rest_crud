package service;

import dto.OrderDto;
import entity.Order;
import lombok.SneakyThrows;
import mapper.Mapper;
import mapper.OrderMapper;
import repository.OrderRepository;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDto> mapper = new OrderMapper();

    public OrderServiceImpl() {
        this.orderRepository = new OrderRepository();
    }

    @Override
    public List<OrderDto> getAllOrder() {
        List<Order> orderList = orderRepository.getAllOrder();
        return orderList.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return mapper.toDto(orderRepository.getOrderById(id));
    }


    @Override
    public OrderDto saveOrder(OrderDto orderDto) {
        Order order = mapper.toEntity(orderDto);
        Order savedOrder = orderRepository.saveOrder(order);
        return mapper.toDto(savedOrder);
    }

    @Override
    public OrderDto saveOrder(OrderDto orderDto, List<Long> productsId) {
        Order order = mapper.toEntity(orderDto);
        Order savedOrder = orderRepository.saveOrder(order, productsId);
        return mapper.toDto(savedOrder);
    }

    @Override
    public OrderDto updateOrder(OrderDto orderDto) {
        Order order = mapper.toEntity(orderDto);
        Order updatedOrder = orderRepository.updateOrder(order);
        return mapper.toDto(updatedOrder);
    }

    @Override
    public boolean deleteOrder(Long id) {
        return orderRepository.deleteOrder(id);
    }
}
