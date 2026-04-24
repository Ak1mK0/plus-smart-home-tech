package ru.yandex.practicum.telemetry.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import telemetry.service.event.HubEventProto;

public interface HubEventHandler {
    public HubEventProto.PayloadCase getMessageType();

    void handle(HubEventProto event, Producer<String, SpecificRecordBase> producer);
}
