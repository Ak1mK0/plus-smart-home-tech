package ru.yandex.practicum.telemetry.collector.handler.sensors.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import telemetry.service.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.handler.sensors.SensorEventHandler;

@Slf4j
@Component
public class MotionSensorEventHandler implements SensorEventHandler {

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.MOTION_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event, Producer<String, byte[]> producer) {
        log.debug("MotionSensorEventHandler request = {}", event);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                "telemetry.sensors.v1",
                event.getHubId(),
                event.toByteArray()
        );
        try {
            producer.send(record).get(); // ждем подтверждения
            log.info("Message sent successfully");
        } catch (Exception e) {
            log.error("Failed to send message", e);
            throw new RuntimeException(e);
        }
    }
}
