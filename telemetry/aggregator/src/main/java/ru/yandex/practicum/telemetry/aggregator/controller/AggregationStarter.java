package ru.yandex.practicum.telemetry.aggregator.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

@Slf4j
@Component
public class AggregationStarter {
    private final Consumer<String, SensorEventAvro> avroConsumer;
    private final Producer<String, SpecificRecordBase> avroProducer;
    private final Map<String, SensorsSnapshotAvro> snapshotAvroMap = new HashMap<>();

    public AggregationStarter () {
        this.avroProducer = initProducer();
        this.avroConsumer = initConsumer();
    }

    public void start() {
        try {
            avroConsumer.subscribe(List.of("telemetry.sensors.v1"));

            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        avroConsumer.poll(Duration.ofSeconds(5));

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    Optional<SensorsSnapshotAvro> optionalSensorsSnapshotAvro = updateState(record);

                    if (optionalSensorsSnapshotAvro.isPresent()) {
                        SensorsSnapshotAvro sensorsSnapshotAvro = optionalSensorsSnapshotAvro.get();
                        snapshotAvroMap.put(sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro);
                        avroProducer.send(new ProducerRecord<>("telemetry.snapshots.v1",
                                sensorsSnapshotAvro.getHubId(), sensorsSnapshotAvro));
                    }

                }
            }

        } catch (WakeupException ignored) {

        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедиться,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                // здесь нужно вызвать метод консьюмера для фиксации смещений

            } finally {
                log.info("Закрываем консьюмер");
                avroConsumer.close();
                log.info("Закрываем продюсер");
                avroProducer.close();
            }
        }
    }


    private Optional<SensorsSnapshotAvro> updateState(ConsumerRecord<String, SensorEventAvro> record) {
        SensorEventAvro avro = record.value();
        log.debug("Get {} data: {}", avro.getPayload().getClass().getSimpleName(), avro);

        if (!snapshotAvroMap.containsKey(record.key())) {
            SensorsSnapshotAvro snapshot = newHubInMap(avro);
            return Optional.of(snapshot);
        }
        return Optional.ofNullable(null);
    }

    private SensorsSnapshotAvro newHubInMap(SensorEventAvro avro) {
        Map<String, SensorStateAvro> sensorStateMap = new HashMap<>();
        sensorStateMap.put(avro.getId(), toStateAvro(avro));
        return SensorsSnapshotAvro.newBuilder()
                .setHubId(avro.getHubId())
                .setTimestamp(avro.getTimestamp())
                .setSensorsState(sensorStateMap)
                .build();
    }

    private SensorStateAvro toStateAvro(SensorEventAvro avro) {
        return SensorStateAvro.newBuilder()
                .setTimestamp(avro.getTimestamp())
                .setData(avro)
                .build();
    }

    private Consumer<String, SensorEventAvro> initConsumer()  {
        Properties config = new Properties();
        config.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "deserializer.SensorEventDeserializer");
        config.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "sensor-aggregator-group");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(config);
    }

    private Producer<String, SpecificRecordBase> initProducer()  {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "serializer.AvroSerializer");

        return new KafkaProducer<>(config);
    }
}
