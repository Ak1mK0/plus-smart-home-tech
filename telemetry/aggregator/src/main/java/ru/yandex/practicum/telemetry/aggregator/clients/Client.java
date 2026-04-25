package ru.yandex.practicum.telemetry.aggregator.clients;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

public interface Client {

    Consumer<String, SensorEventAvro> getConsumer();

    void stop();

    Producer<String, SpecificRecordBase> getProducer();
}
