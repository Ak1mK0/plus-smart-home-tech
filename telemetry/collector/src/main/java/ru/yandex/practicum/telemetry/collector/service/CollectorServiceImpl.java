package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.telemetry.collector.model.hub.abstractModel.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.sensors.abstractModel.SensorEvent;

import java.util.Properties;

@Service
@Slf4j
public class CollectorServiceImpl implements CollectorService {

    private Producer<String, SpecificRecordBase> producer;

    public CollectorServiceImpl() {
        this.producer = initProducer();
        log.info("Created default Kafka producer");
    }

    public void saveSensorEvent(SensorEvent sensorEvent) {
        SpecificRecordBase data = sensorEvent.toAvro();
        log.debug("{}: {}", sensorEvent.getClass().getSimpleName(), data);
        producer.send(new ProducerRecord<>("telemetry.sensors.v1", sensorEvent.getClass().getSimpleName(), data));
    }

    public void saveHubEvent(HubEvent hubEvent) {
        SpecificRecordBase data = hubEvent.toAvro();
        log.debug("{}: {}", hubEvent.getClass().getSimpleName(), data);
        producer.send(new ProducerRecord<>("telemetry.hubs.v1", hubEvent.getClass().getSimpleName(), data));
    }

    private Producer<String, SpecificRecordBase> initProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ru.yandex.practicum.telemetry.collector.service.AvroSerializer");

        return new KafkaProducer<>(config);
    }
}
