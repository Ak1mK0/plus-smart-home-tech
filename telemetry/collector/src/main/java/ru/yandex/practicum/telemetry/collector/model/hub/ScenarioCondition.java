package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

@Getter
@Setter
public class ScenarioCondition {
    private String sensorId;
    private ConditionTypeAvro type;
    private ConditionOperationAvro operation;
    private int value;

    public ScenarioConditionAvro toAvro() {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(getSensorId())
                .setType(getType())
                .setOperation(getOperation())
                .setValue(getValue())
                .build();
    }
}
