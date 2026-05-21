package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewProductInWarehouseRequest {
    @NotNull(message = "ID продукта обязателен")
    private UUID productId;
    private boolean fragile;
    private DimensionDto dimension;
    @DecimalMin(value = "1.0", message = "Вес должен быть больше или равен 1.0")
    private double weight;
}
