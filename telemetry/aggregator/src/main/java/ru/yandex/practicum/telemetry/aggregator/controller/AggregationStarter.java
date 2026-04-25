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

        } else {
            SensorsSnapshotAvro existingSnapshot = snapshotsAvro.get(avro.getHubId());

            if (!avro.getTimestamp().isBefore(existingSnapshot.getTimestamp())) {
                log.debug("Old SensorStateAvro: {}", snapshotsAvro.get(avro.getHubId()));
                Map<String, SensorStateAvro> updatedStates = new HashMap<>(existingSnapshot.getSensorsState());
                updatedStates.put(avro.getId(), toStateAvro(avro));

                SensorsSnapshotAvro newSnapshot = SensorsSnapshotAvro.newBuilder()
                        .setHubId(avro.getHubId())
                        .setTimestamp(avro.getTimestamp())
                        .setSensorsState(updatedStates)
                        .build();

                snapshotsAvro.put(avro.getHubId(), newSnapshot);
                log.debug("New SensorStateAvro: {}", snapshotsAvro.get(avro.getHubId()));

                return Optional.of(newSnapshot);

            } else {
                return Optional.empty();
            }
        }
    }

    private SensorStateAvro toStateAvro(SensorEventAvro avro) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(avro.getTimestamp())
                .setData(avro.getPayload())
                .build();
    }


}
