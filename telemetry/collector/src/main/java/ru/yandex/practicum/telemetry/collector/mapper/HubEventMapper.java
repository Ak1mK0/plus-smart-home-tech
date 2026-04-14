package ru.yandex.practicum.telemetry.collector.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.hub.*;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        HubEventAvro.Builder builder = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp());

        switch (event) {
            case DeviceAddedEvent deviceAdded -> {
                DeviceAddedEventAvro payload = DeviceAddedEventAvro.newBuilder()
                        .setId(deviceAdded.getId())
                        .setType(mapDeviceType(deviceAdded.getDeviceType()))
                        .build();
                builder.setPayload(payload);

            }
            case DeviceRemovedEvent deviceRemoved -> {
                DeviceRemovedEventAvro payload = DeviceRemovedEventAvro.newBuilder()
                        .setId(deviceRemoved.getId())
                        .build();
                builder.setPayload(payload);

            }
            case ScenarioAddedEvent scenarioAdded -> {
                ScenarioAddedEventAvro payload = ScenarioAddedEventAvro.newBuilder()
                        .setName(scenarioAdded.getName())
                        .setConditions(mapConditions(scenarioAdded.getConditions()))
                        .setActions(mapActions(scenarioAdded.getActions()))
                        .build();
                builder.setPayload(payload);

            }
            case ScenarioRemovedEvent scenarioRemoved -> {
                ScenarioRemovedEventAvro payload = ScenarioRemovedEventAvro.newBuilder()
                        .setName(scenarioRemoved.getName())
                        .build();
                builder.setPayload(payload);
            }
            default -> {
            }
        }

        return builder.build();
    }

    private DeviceTypeAvro mapDeviceType(DeviceType type) {
        return switch (type) {
            case MOTION_SENSOR -> DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR -> DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR -> DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR -> DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR -> DeviceTypeAvro.SWITCH_SENSOR;
            default -> throw new IllegalArgumentException("Unknown device type: " + type);
        };
    }

    private List<ScenarioConditionAvro> mapConditions(List<ScenarioCondition> conditions) {
        return conditions.stream().map(condition ->
                ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setOperation(mapOperation(condition.getOperation()))
                        .setType(mapConditionType(condition.getType()))
                        .setValue(condition.getValue())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<DeviceActionAvro> mapActions(List<DeviceAction> actions) {
        return actions.stream().map(action ->
                DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(mapActionType(action.getType()))
                        .setValue(action.getValue())
                        .build()
        ).collect(Collectors.toList());
    }

    private ConditionOperationAvro mapOperation(ScenarioConditionOperation operation) {
        if (operation == null) {
            return null;
        }
        return switch (operation) {
            case EQUALS -> ConditionOperationAvro.EQUALS;
            case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }

    private ConditionTypeAvro mapConditionType(ScenarioConditionType type) {
        if (type == null) {
            return null;
        }
        return switch (type) {
            case MOTION -> ConditionTypeAvro.MOTION;
            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
            case SWITCH -> ConditionTypeAvro.SWITCH;
            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
            default -> throw new IllegalArgumentException("Unknown condition type: " + type);
        };
    }

    private ActionTypeAvro mapActionType(DeviceActionType type) {
        if (type == null) {
            return null;
        }

        return switch (type) {
            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
            case INVERSE -> ActionTypeAvro.INVERSE;
            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
            default -> throw new IllegalArgumentException("Unknown action type: " + type);
        };
    }
}
