package ru.yandex.practicum.telemetry.collector.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;
import ru.yandex.practicum.telemetry.collector.service.CollectorService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    @ResponseStatus(HttpStatus.OK)
    public void sensorEvent(@RequestBody SensorEvent sensorEvent) {
        log.debug("SensorEvent: {}", sensorEvent);
        collectorService.saveSensorEvent(sensorEvent);
    }

    @PostMapping("/hubs")
    @ResponseStatus(HttpStatus.OK)
    public void hubEvent(@RequestBody HubEvent hubEvent) {
        log.debug("HubEvent: {}", hubEvent);
        collectorService.saveHubEvent(hubEvent);
    }
}
