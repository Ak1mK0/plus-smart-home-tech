package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Payment;
import ru.yandex.practicum.model.PaymentStatus;
import ru.yandex.practicum.repository.PaymentRepository;
import ru.yandex.practicum.service.PaymentService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Loggable
    @Transactional
    public Payment createPayment(@RequestBody Payment payment) {
        if (payment.getTotalPayment() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Total payment cannot be null or zero");
        }
        if (payment.getDeliveryTotal() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery total cannot be null or zero");
        }
        if (payment.getFeeTotal() == 0.0) {
            throw new NotEnoughInfoInOrderToCalculateException("Fee total cannot be null or zero");
        }
        return paymentRepository.save(payment);
    }

    public double calculateTotalCost(@RequestBody OrderDto order) {
        return 0;
    }

    public double calculateProductCost(@RequestBody OrderDto order) {
        return 0;
    }

    @Loggable
    @Transactional
    public void successPayment(@RequestBody UUID orderId) {
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
