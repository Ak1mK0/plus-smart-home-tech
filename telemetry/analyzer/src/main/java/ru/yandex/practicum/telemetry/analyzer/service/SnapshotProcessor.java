package ru.yandex.practicum.telemetry.analyzer.service;

import deserializer.SnapshotEventDeserializer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.client.KafkaClientConfigurationImpl;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {
    private final List<String> TOPICS = List.of("telemetry.snapshots.v1");
    private final String GROUPID = "snapshot-analyzer-group";

    private final KafkaClientConfigurationImpl<SensorsSnapshotAvro> client;
    private Consumer<String, SensorsSnapshotAvro> consumer;
    private final Map<String, SensorsSnapshotAvro> snapshot = new HashMap<>();

    @PostConstruct
    public void init() {
        this.consumer = client.initConsumer(GROUPID, SnapshotEventDeserializer.class);
    }

    public void start() {
        try {
            consumer.subscribe(TOPICS);

            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records =
                        consumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    log.debug("New SNAPSHOT data in {}: Тип {} - {}", record.key(),
                            record.value().getClass().getSimpleName(),
                            record.value());
                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время получения или обработки снимка", e);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }
}
