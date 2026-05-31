package ru.yandex.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    @NotNull(message = "ID продукта обязателен")
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    @NotNull(message = "ID заказа обязателен")
    private UUID orderId;
    @NotNull(message = "Статус продукта не может быть пустым")
    private DeliveryStateDto deliveryState;
}
