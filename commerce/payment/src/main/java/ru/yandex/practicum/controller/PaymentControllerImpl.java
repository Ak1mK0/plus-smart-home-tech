package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.controller.payment.PaymentController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.model.mapper.PaymentMapper;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Validated
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @Loggable
    @PostMapping
    public PaymentDto createPayment(@RequestBody OrderDto order) {
        Payment payment = Payment.builder()
                .paymentId(order.getPaymentId())
                .totalPayment(order.getTotalPrice())
                .deliveryTotal(order.getDeliveryPrice())
                .feeTotal(order.getTotalPrice() - order.getDeliveryPrice() - order.getProductPrice())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
        payment = paymentService.createPayment(payment);
        return paymentMapper.toDto(payment);
    }

    @Loggable
    @PostMapping("/totalCost")
    public double calculateTotalCost(@RequestBody OrderDto order) {
        return 0;
    }

    @Loggable
    @PostMapping("/refund")
    public void successPayment(@RequestBody UUID orderId) {
        paymentService.successPayment(orderId);
    }

    @Loggable
    @PostMapping("/productCost")
    public double calculateProductCost(@RequestBody OrderDto order) {
        return 0;
    }

    @Loggable
    @PostMapping("/failed")
    public void failedPayment(@RequestBody UUID orderId) {
        paymentService.failedPayment(orderId);
    }
}
