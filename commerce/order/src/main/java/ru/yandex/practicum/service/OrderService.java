package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.model.Order;

public interface OrderService {

    Order createNewOrder(CreateNewOrderRequest newOrder, BookedProductsDto bookedProducts);
}
