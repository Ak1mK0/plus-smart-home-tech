package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.service.PaymentService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Loggable
    @Transactional
    public Payment createPayment(Payment payment) {
        if (payment.getTotalPayment() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Total payment cannot be zero");
        }
        if (payment.getDeliveryTotal() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery total cannot be zero");
        }
        if (payment.getFeeTotal() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Fee total cannot be zero");
        }
        return paymentRepository.save(payment);
    }

    @Loggable
    public double calculateTotalCost(double deliveryPrice, double productsPrice) {
        if (deliveryPrice == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery total cannot be zero");
        }
        if (productsPrice == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Product total cannot be zero");
        }
        return productsPrice * 0.1 + productsPrice + deliveryPrice;
    }

    @Loggable
    public double calculateProductCost(List<ProductDto> products, Map<UUID, Integer> productsQuantity) {
        if (products.isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("List of products cannot be empty");
        }
        return products.stream()
                .mapToDouble(p -> p.getPrice() * productsQuantity.get(p.getProductId()))
                .sum();
    }

    @Loggable
    @Transactional
    public void successPayment(UUID orderId) {
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow((() -> new NoOrderFoundException("Order with id: " + orderId + " not found")));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Loggable
    @Transactional
    public void failedPayment(@RequestBody UUID orderId) {
        Payment payment = paymentRepository.findById(orderId)
                .orElseThrow((() -> new NoOrderFoundException("Order with id: " + orderId + " not found")));
        payment.setPaymentStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }
}
