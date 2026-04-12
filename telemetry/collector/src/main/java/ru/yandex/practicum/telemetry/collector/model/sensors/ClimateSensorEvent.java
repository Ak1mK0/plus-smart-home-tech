package ru.yandex.practicum.telemetry.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class ClimateSensorEvent extends SensorEvent {
//    уровень температуры по шкале C
    private int temperatureC;
//    влажность
    private int humidity;
//    уровень CO2
    private int co2Level;

    @Override
    public SensorEventType getType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
} 