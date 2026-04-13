package ru.yandex.practicum.telemetry.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class TemperatureSensorEvent extends SensorEvent {
    //    температура в С
    private int temperatureC;
    //    температура в F
    private int temperatureF;

    @Override
    public SensorEventType getType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
} 