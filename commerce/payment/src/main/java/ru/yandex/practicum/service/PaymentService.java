package ru.yandex.practicum.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Payment;

import java.util.UUID;

public interface PaymentService {


    Payment createPayment(@RequestBody Payment payment);

    double calculateTotalCost(@RequestBody OrderDto order);

    void successPayment(@RequestBody UUID orderId);

    double calculateProductCost(@RequestBody OrderDto order);

    void failedPayment(@RequestBody UUID orderId);
}
