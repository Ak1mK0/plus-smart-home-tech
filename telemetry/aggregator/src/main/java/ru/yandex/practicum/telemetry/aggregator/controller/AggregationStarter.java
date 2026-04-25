package ru.yandex.practicum.telemetry.aggregator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.aggregator.clients.Client;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {
    private final Client client;
    private final Map<String, SensorsSnapshotAvro> snapshotAvro = new HashMap<>();

    public void start() {
        try {
            client.getConsumer().subscribe(List.of("telemetry.sensors.v1"));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        client.getConsumer().poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    SensorEventAvro convertedEvent = convertSensorEvent(record.value());
                    Optional<SensorsSnapshotAvro> optionalSensorsSnapshotAvro = updateState(convertedEvent);
                    log.debug("Actual snapshotAvro = {}", snapshotAvro);
                    optionalSensorsSnapshotAvro.ifPresent(sensorsSnapshotAvro ->
                            client.getProducer().send(new ProducerRecord<>("telemetry.snapshots.v1",
                                    sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro)));
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                client.getProducer().flush();
                client.getConsumer().commitSync();
            } finally {
                log.info("Закрываем консьюмер и продюсер");
                client.stop();
            }
        }
    }

    private SensorEventAvro convertSensorEvent(SensorEventAvro event) {
        Object payload = event.getPayload();

        if (payload instanceof SpecificRecord) {
            return event;
        }

        if (payload instanceof GenericData.Record) {
            GenericData.Record payloadRecord = (GenericData.Record) payload;
            SpecificRecord convertedPayload = convertPayload(payloadRecord);

            return SensorEventAvro.newBuilder()
                    .setId(event.getId())
                    .setHubId(event.getHubId())
                    .setTimestamp(event.getTimestamp())
                    .setPayload(convertedPayload)
                    .build();
        }
        log.warn("Unknown payload type: {}", payload.getClass());
        return event;
    }

    private SpecificRecord convertPayload(GenericData.Record payloadRecord) {
        String recordName = payloadRecord.getSchema().getName();

        switch (recordName) {
            case "ClimateSensorAvro":
                return ClimateSensorAvro.newBuilder()
                        .setTemperatureC((Integer) payloadRecord.get("temperature_c"))
                        .setHumidity((Integer) payloadRecord.get("humidity"))
                        .setCo2Level((Integer) payloadRecord.get("co2_level"))
                        .build();

            case "LightSensorAvro":
                return LightSensorAvro.newBuilder()
                        .setLinkQuality((Integer) payloadRecord.get("link_quality"))
                        .setLuminosity((Integer) payloadRecord.get("luminosity"))
                        .build();

            case "MotionSensorAvro":
                return MotionSensorAvro.newBuilder()
                        .setLinkQuality((Integer) payloadRecord.get("link_quality"))
                        .setMotion((Boolean) payloadRecord.get("motion"))
                        .setVoltage((Integer) payloadRecord.get("voltage"))
                        .build();

            case "SwitchSensorAvro":
                return SwitchSensorAvro.newBuilder()
                        .setState((Boolean) payloadRecord.get("state"))
                        .build();

            case "TemperatureSensorAvro":
                Long timestampMillis = (Long) payloadRecord.get("timestamp");
                Instant instant = Instant.ofEpochMilli(timestampMillis);

                return TemperatureSensorAvro.newBuilder()
                        .setId(payloadRecord.get("id").toString())
                        .setHubId(payloadRecord.get("hubId").toString())
                        .setTimestamp(instant)
                        .setTemperatureC((Integer) payloadRecord.get("temperature_c"))
                        .setTemperatureF((Integer) payloadRecord.get("temperature_f"))
                        .build();

            default:
                throw new IllegalArgumentException("Неизвестный тип датчика: " + recordName);
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro avro) {
        log.debug("Get {} data: {}", avro.getPayload().getClass().getSimpleName(), avro);

        if (!snapshotAvro.containsKey(avro.getHubId())) {
            Map<String, SensorStateAvro> sensorStateMap = new HashMap<>();
            sensorStateMap.put(avro.getId(), toStateAvro(avro));

            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(avro.getHubId())
                    .setTimestamp(Instant.now())
                    .setSensorsState(sensorStateMap)
                    .build();
            snapshotAvro.put(avro.getHubId(), snapshot);

            return Optional.of(snapshot);

        } else {
            SensorsSnapshotAvro existingSnapshot = snapshotAvro.get(avro.getHubId());

            if (avro.getTimestamp().isAfter(existingSnapshot.getTimestamp())) {
                Map<String, SensorStateAvro> updatedStates = new HashMap<>(existingSnapshot.getSensorsState());
                updatedStates.put(avro.getId(), toStateAvro(avro));

                SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                        .setHubId(avro.getHubId())
                        .setTimestamp(avro.getTimestamp())
                        .setSensorsState(updatedStates)
                        .build();

                snapshotAvro.put(avro.getHubId(), newSnapshot);
                return Optional.of(newSnapshot);

            } else {
                return Optional.empty();
            }
        }
    }

    private SensorStateAvro toStateAvro(SensorEventAvro avro) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(avro.getTimestamp())
                .setData(avro)
                .build();
    }


}
