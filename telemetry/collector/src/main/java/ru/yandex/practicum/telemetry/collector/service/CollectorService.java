package ru.yandex.practicum.telemetry.collector.service;

import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;

public interface CollectorService {

    void saveSensorEvent(SensorEvent sensorEvent);

    void saveHubEvent(HubEvent hubEvent);
}
