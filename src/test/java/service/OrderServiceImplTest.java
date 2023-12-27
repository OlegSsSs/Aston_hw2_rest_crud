package service;

import dto.OrderDto;
import entity.Order;
import mapper.Mapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private Mapper<Order, OrderDto> mapper;
    private OrderServiceImpl orderService;
    private OrderDto supposedOrderDto;
    private Order supposedOrder;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, mapper);

        supposedOrderDto = OrderDto.builder()
              .id(1L)
              .name("test name order")
              .build();

        supposedOrder = new Order();
        supposedOrder.setId(1L);
        supposedOrder.setName("test name order");
    }

    @Test
    void testGetOrder() {
        when(orderRepository.getOrderById(anyLong())).thenReturn(supposedOrder);
        when(orderService.getOrderById(anyLong())).thenReturn(supposedOrderDto);

        OrderDto actualOrderDto = orderService.getOrderById(1L);

        verify(mapper).toDto(supposedOrder);
        assertEquals(supposedOrderDto, actualOrderDto);
    }

    @Test
    void testSaveOrder() {
        when(orderRepository.saveOrder(any(Order.class))).thenReturn(supposedOrder);
        when(mapper.toEntity(any(OrderDto.class))).thenReturn(supposedOrder);

        orderService.saveOrder(supposedOrderDto);

        verify(mapper).toEntity(supposedOrderDto);
        verify(orderRepository).saveOrder(supposedOrder);
        verify(mapper).toDto(supposedOrder);
    }

    @Test
    void testUpdateOrder() {
        when(mapper.toEntity(any(OrderDto.class))).thenReturn(supposedOrder);
        when(orderRepository.updateOrder(any(Order.class))).thenReturn(supposedOrder);
        when(mapper.toDto(any(Order.class))).thenReturn(supposedOrderDto);

        OrderDto actualOrderDto = orderService.updateOrder(supposedOrderDto);

        verify(mapper).toEntity(supposedOrderDto);
        verify(orderRepository).updateOrder(supposedOrder);
        verify(mapper).toDto(supposedOrder);
        assertEquals(supposedOrderDto, actualOrderDto);
    }
}