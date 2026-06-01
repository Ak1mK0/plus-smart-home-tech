package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class ShippedToDeliveryRequest {
    @NotNull(message = "ID заказа обязательно")
    private UUID orderId;
    @NotNull(message = "ID доставки обязателен")
    private UUID deliveryId;
}
