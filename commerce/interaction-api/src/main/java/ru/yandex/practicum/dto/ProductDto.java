package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Не верный формат UUID")
    private String productId;
    @NotNull(message = "Имя продукта не может быть пустым")
    private String productName;
    @NotNull(message = "Описание продукта не может быть пустым")
    private String description;
    private String imageSrc;
    @NotNull(message = "Количество продукта не может быть пустым")
    private QuantityState quantityState;
    @NotNull(message = "Статус продукта не может быть пустым")
    private ProductState productState;
    private ProductCategory productCategory;
    @DecimalMin(value = "1.0", message = "Цена продукта должна быть больше или равна 1.0")
    private double price;
}
