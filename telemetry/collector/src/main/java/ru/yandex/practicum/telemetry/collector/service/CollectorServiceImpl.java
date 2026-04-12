package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;

@Service
@Slf4j
public class CollectorServiceImpl implements CollectorService {

    public void saveSensorEvent(SensorEvent sensorEvent) {

    }

    public void saveHubEvent(HubEvent hubEvent) {

    }
}
