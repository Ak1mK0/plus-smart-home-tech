package ru.yandex.practicum.telemetry.collector.model.sensors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEventType;

@Getter
@Setter
@ToString(callSuper = true)
public class LightSensorEvent extends SensorEvent {
    //    качество связи
    private int linkQuality;
    //    уровень освещённости
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
} 