package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Payment;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {


    Payment createPayment(Payment payment);

    double calculateTotalCost(double deliveryPrice, double productsPrice);

    void successPayment(UUID orderId);

    double calculateProductCost(List<ProductDto> products, Map<UUID, Integer> productsQuantity);

    void failedPayment(UUID orderId);
}
