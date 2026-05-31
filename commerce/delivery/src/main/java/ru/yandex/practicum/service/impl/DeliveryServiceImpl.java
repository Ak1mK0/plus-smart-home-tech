package ru.yandex.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.logging.Loggable;
import ru.yandex.practicum.model.Delivery;
import ru.yandex.practicum.model.DeliveryState;
import ru.yandex.practicum.repository.DeliveryRepository;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {
    final DeliveryRepository deliveryRepository;

    @Loggable
    @Transactional
    public Delivery createNewDelivery(Delivery delivery) {
        delivery.setDeliveryState(DeliveryState.CREATED);
        return deliveryRepository.save(delivery);
    }

    @Loggable
    @Transactional
    public Delivery successfulDelivery(UUID id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoDeliveryFoundException("Deliver with ID: " + id + "does not exist"));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        return deliveryRepository.save(delivery);
    }

    @Loggable
    @Transactional
    public Delivery pickedDelivery(UUID id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoDeliveryFoundException("Deliver with ID: " + id + "does not exist"));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        return deliveryRepository.save(delivery);
    }

    @Loggable
    @Transactional
    public Delivery failedDelivery(UUID id) {
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NoDeliveryFoundException("Deliver with ID: " + id + "does not exist"));
        delivery.setDeliveryState(DeliveryState.FAILED);
        return deliveryRepository.save(delivery);
    }

    @Loggable
    public double calculateDeliveryCost(OrderDto dto) {
        return 0;
    }

    @Loggable
    public Delivery findByDeliveryId(UUID id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new NoDeliveryFoundException("Deliver with ID: " + id + "does not exist"));
    }
}
