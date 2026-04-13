package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScenarioCondition {
    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperation operation;
    private int value;
}
