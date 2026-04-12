package ru.yandex.practicum.telemetry.collector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.service.CollectorService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public void sensorEvent (@RequestBody SensorEvent sensorEvent) {
        log.debug("SensorEvent: {}", sensorEvent);
        collectorService.saveSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    public void hubEvent (@RequestBody HubEvent hubEvent) {
        log.debug("HubEvent: {}", hubEvent);
        collectorService.saveHubEvent(hubEvent);
    }
}
