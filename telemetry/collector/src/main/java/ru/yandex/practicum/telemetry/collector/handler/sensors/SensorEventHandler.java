package ru.yandex.practicum.telemetry.collector.handler.sensors;

import org.apache.kafka.clients.producer.Producer;
import telemetry.service.event.SensorEventProto;

public interface SensorEventHandler {
    SensorEventProto.PayloadCase getMessageType();

    void handle(SensorEventProto event, Producer<String, byte[]> producer);
}
