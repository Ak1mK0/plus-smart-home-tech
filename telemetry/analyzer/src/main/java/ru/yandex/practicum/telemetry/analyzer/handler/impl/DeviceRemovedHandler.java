package ru.yandex.practicum.telemetry.analyzer.handler.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.telemetry.analyzer.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceRemovedHandler implements HubEventHandler {
    private final SensorRepository sensorRepository;

    @Override
    public String getEvent() {
        return DeviceRemovedEventAvro.class.getSimpleName();
    }

    @Override
    public void handle(HubEventAvro event) {
        log.debug("Start removing data");
        DeviceRemovedEventAvro removedEventAvro = (DeviceRemovedEventAvro) event.getPayload();
        sensorRepository.deleteById(removedEventAvro.getId());
    }
}
