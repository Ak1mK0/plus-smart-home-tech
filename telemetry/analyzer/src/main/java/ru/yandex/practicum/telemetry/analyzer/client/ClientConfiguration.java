package ru.yandex.practicum.telemetry.analyzer.client;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;


public interface ClientConfiguration<T extends SpecificRecordBase> {

    Consumer<String, T> getConsumer(String groupId);

    void stop();
}
