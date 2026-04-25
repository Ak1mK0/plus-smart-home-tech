package ru.yandex.practicum.telemetry.aggregator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
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
    private final Map<String, SensorsSnapshotAvro> snapshotsAvro = new HashMap<>();

    public void start() {
        try {
            client.getConsumer().subscribe(List.of("telemetry.sensors.v1"));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        client.getConsumer().poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> optionalSensorsSnapshotAvro = updateState(record.value());
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

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro avro) {
        log.debug("Get {} data: {}", avro.getPayload().getClass().getSimpleName(), avro);

        if (!snapshotsAvro.containsKey(avro.getHubId())) {
            Map<String, SensorStateAvro> sensorStateMap = new HashMap<>();
            sensorStateMap.put(avro.getId(), toStateAvro(avro));

            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(avro.getHubId())
                    .setTimestamp(avro.getTimestamp())
                    .setSensorsState(sensorStateMap)
                    .build();
            snapshotsAvro.put(avro.getHubId(), snapshot);

            return Optional.of(snapshot);
        }

        SensorsSnapshotAvro existingSnapshot = snapshotsAvro.get(avro.getHubId());
        Map<String, SensorStateAvro> currentStates = existingSnapshot.getSensorsState();

        if (currentStates.containsKey(avro.getId())) {
            SensorStateAvro currentSensorState = currentStates.get(avro.getId());

            if (avro.getTimestamp().isBefore(currentSensorState.getTimestamp())) {
                log.debug("Skipping sensor {} - event timestamp {} is not after current {}",
                        avro.getId(), avro.getTimestamp(), currentSensorState.getTimestamp());
                return Optional.empty();
            }

            if (avro.getTimestamp().equals(currentSensorState.getTimestamp()) &&
                    currentSensorState.getData().equals(avro.getPayload())) {
                log.debug("Same timestamp and same data for sensor {}, skipping", avro.getId());
                return Optional.empty();
            }

            log.debug("Updating sensor {} - timestamp: {}, data changed",
                    avro.getId(), avro.getTimestamp());
        }

        Map<String, SensorStateAvro> updatedStates = new HashMap<>(currentStates);
        updatedStates.put(avro.getId(), toStateAvro(avro));

        Instant maxTimestamp = updatedStates.values().stream()
                .map(SensorStateAvro::getTimestamp)
                .max(Instant::compareTo)
                .orElse(avro.getTimestamp());

        SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                .setHubId(avro.getHubId())
                .setTimestamp(maxTimestamp)
                .setSensorsState(updatedStates)
                .build();

        snapshotsAvro.put(avro.getHubId(), newSnapshot);
        log.debug("Updated snapshot for hub {}, max timestamp: {}", avro.getHubId(), maxTimestamp);

        return Optional.of(newSnapshot);
    }

    private SensorStateAvro toStateAvro(SensorEventAvro avro) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(avro.getTimestamp())
                .setData(avro.getPayload())
                .build();
    }
}
