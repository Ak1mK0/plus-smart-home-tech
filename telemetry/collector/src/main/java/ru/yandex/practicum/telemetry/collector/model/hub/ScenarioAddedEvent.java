package ru.yandex.practicum.telemetry.collector.model.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    private String name;
    private List<ScenarioCondition> conditions;
    private List<DeviceActionAvro> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

    @Override
    public SpecificRecordBase toAvro() {
        List<ScenarioConditionAvro> conditionAvros = conditions.stream()
                .map(ScenarioCondition::toAvro)
                .collect(Collectors.toList());

        return ScenarioAddedEventAvro.newBuilder()
                .setName(getName())
                .setConditions(conditionAvros)
                .setActions(actions)
                .build();
    }
}
