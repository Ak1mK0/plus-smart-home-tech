package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.controller.order.OrderController;
import ru.yandex.practicum.controller.warehouse.feign.WarehouseControllerFeign;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.model.Order;
import ru.yandex.practicum.model.mapper.OrderMapper;
import ru.yandex.practicum.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Validated
public class OrderControllerImpl implements OrderController {
    private OrderService orderService;
    private OrderMapper orderMapper;
    private WarehouseControllerFeign warehouseControllerFeign;

    @GetMapping
    public List<OrderDto> getAllUserOrders(@RequestParam String username,
                                    @PageableDefault(size = 20, sort = "username",
                                            direction = Sort.Direction.ASC) Pageable pageable) {
        return null;
    }

    @PutMapping
    public OrderDto createNewOrder(@RequestBody CreateNewOrderRequest newOrder) {
        BookedProductsDto bookedProductsDto =
                warehouseControllerFeign.checkAvailableAllProductInShoppingCart(newOrder.getShoppingCartDto());
        Order order = orderService.createNewOrder(newOrder, bookedProductsDto);
        return orderMapper.toDto(order);
    }

    @PostMapping("/return")
    public OrderDto returnProducts(@RequestBody ProductReturnRequest request) {
        return null;
    }

    @PostMapping("/payment")
    public OrderDto payForOrder(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/payment/failed")
    public OrderDto failedPayForOrder(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/delivery")
    public OrderDto orderDelivered(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/delivery/failed")
    public OrderDto failedOrderDelivered(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/completed")
    public OrderDto completeOrder(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotalCost(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDeliveryCost(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/assembly")
    public OrderDto assemblyOrder(@RequestBody UUID orderId) {
        return null;
    }

    @PostMapping("/assembly/failed")
    public OrderDto failedAssemblyOrder(@RequestBody UUID orderId) {
        return null;
    }
}
