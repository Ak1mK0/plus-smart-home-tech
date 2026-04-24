package ru.yandex.practicum.telemetry.collector.handler.hub.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import telemetry.service.event.HubEventProto;
import ru.yandex.practicum.telemetry.collector.handler.hub.HubEventHandler;

@Slf4j
@Component
public class ScenarioRemovedEventHandler implements HubEventHandler {

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_REMOVED;
    }

    @Override
    public void handle(HubEventProto event, Producer<String, byte[]> producer) {
        log.debug("ScenarioRemovedEventHandler request = {}", event);
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(
                "telemetry.hubs.v1",
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
