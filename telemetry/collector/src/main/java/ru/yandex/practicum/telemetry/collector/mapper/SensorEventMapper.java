package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.sensors.*;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;

@Component
public class SensorEventMapper {

    public SensorEventAvro toAvro(SensorEvent event) {
        SensorEventAvro.Builder builder = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case ClimateSensorEvent climateEvent -> {
                ClimateSensorAvro payload = ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climateEvent.getTemperatureC())
                        .setHumidity(climateEvent.getHumidity())
                        .setCo2Level(climateEvent.getCo2Level())
                        .build();
                builder.setPayload(payload);

            }
            case LightSensorEvent lightEvent -> {
                LightSensorAvro payload = LightSensorAvro.newBuilder()
                        .setLinkQuality(lightEvent.getLinkQuality())
                        .setLuminosity(lightEvent.getLuminosity())
                        .build();
                builder.setPayload(payload);

            }
            case MotionSensorEvent motionEvent -> {
                MotionSensorAvro payload = MotionSensorAvro.newBuilder()
                        .setLinkQuality(motionEvent.getLinkQuality())
                        .setMotion(motionEvent.getMotion())
                        .setVoltage(motionEvent.getVoltage())
                        .build();
                builder.setPayload(payload);

            }
            case SwitchSensorEvent switchEvent -> {
                SwitchSensorAvro payload = SwitchSensorAvro.newBuilder()
                        .setState(switchEvent.getState())
                        .build();
                builder.setPayload(payload);

            }
            case TemperatureSensorEvent tempEvent -> {
                TemperatureSensorAvro payload = TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(tempEvent.getTemperatureC())
                        .setTemperatureF(tempEvent.getTemperatureF())
                        .build();
                builder.setPayload(payload);
            }
            default -> {
            }
        }

        return builder.build();
    }
}
