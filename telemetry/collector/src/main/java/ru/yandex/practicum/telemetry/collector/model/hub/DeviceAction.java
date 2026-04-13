package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

@Getter
@Setter
public class DeviceAction {
    private String sensorId;
    private ActionTypeAvro type;
    private int value;

    public DeviceActionAvro toAvro() {
        return DeviceActionAvro.newBuilder()
                .setSensorId(getSensorId())
                .setType(getType())
                .setValue(getValue())
                .build();
    }
}
