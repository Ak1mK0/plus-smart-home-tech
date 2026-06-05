package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.State;
import ru.yandex.practicum.repository.OrderRepository;
import ru.yandex.practicum.service.OrderService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    public Order createNewOrder(CreateNewOrderRequest newOrder, BookedProductsDto bookedProducts) {
        Order order = Order.builder()
                .shoppingCartId(newOrder.getShoppingCartDto().getShoppingCartId())
                .products(newOrder.getShoppingCartDto().getProducts())
                .paymentId(UUID.randomUUID())
                .deliveryId(UUID.randomUUID())
                .state(State.NEW)
                .deliveryWeight(bookedProducts.getDeliveryWeight())
                .deliveryVolume(bookedProducts.getDeliveryVolume())
                .fragile(bookedProducts.isFragile())
                .totalPrice(0.0)
                .deliveryPrice(0.0)
                .productPrice(0.0)
                .build();
        return orderRepository.save(order);
    }
}
