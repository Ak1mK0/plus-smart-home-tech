package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;
import java.util.UUID;

public class OrderDto {
    @NotNull(message = "ID продукта обязателен")
    private UUID orderId;
    private UUID shoppingCartId;
    @NotNull(message = "Список продуктов обязателен")
    @Size(min = 1, message = "Корзина не может быть пустой")
    Map<UUID, Integer> products;
    private UUID paymentId;
    private UUID deliveryId;
    private StateDto state;
    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;
    private double totalPrice;
    private double deliveryPrice;
    private double productPrice;
}
