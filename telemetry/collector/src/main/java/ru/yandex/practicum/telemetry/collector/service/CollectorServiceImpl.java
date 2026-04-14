package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.telemetry.collector.mapper.HubEventMapper;
import ru.yandex.practicum.telemetry.collector.mapper.SensorEventMapper;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;

import java.time.Duration;
import java.util.Properties;

@Service
@Slf4j
public class CollectorServiceImpl implements CollectorService, AutoCloseable {

    private final Producer<String, SpecificRecordBase> producer;
    private final SensorEventMapper sensorEventMapper;
    private final HubEventMapper hubEventMapper;

    @Autowired
    public CollectorServiceImpl(SensorEventMapper sensorEventMapper,
                                HubEventMapper hubEventMapper) {
        this.sensorEventMapper = sensorEventMapper;
        this.hubEventMapper = hubEventMapper;
        this.producer = initProducer();
        log.info("Created default Kafka producer");
    }

    public void saveSensorEvent(SensorEvent sensorEvent) {
        SensorEventAvro avro = sensorEventMapper.toAvro(sensorEvent);
        log.debug("Sending sensor event: {}", avro);
        producer.send(new ProducerRecord<>("telemetry.sensors.v1", sensorEvent.getHubId(), avro));
        producer.close();
    }

    public void saveHubEvent(HubEvent hubEvent) {
        HubEventAvro avro = hubEventMapper.toAvro(hubEvent);
        log.debug("Sending hub event: {}", avro);
        producer.send(new ProducerRecord<>("telemetry.hubs.v1", hubEvent.getHubId(), avro));
        producer.close();
    }

    private Producer<String, SpecificRecordBase> initProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.serialization.avro.AvroSerializer");

        return new KafkaProducer<>(config);
    }

    @Override
    public void close() {
        producer.flush();
        producer.close(Duration.ofSeconds(10));
    }
}
