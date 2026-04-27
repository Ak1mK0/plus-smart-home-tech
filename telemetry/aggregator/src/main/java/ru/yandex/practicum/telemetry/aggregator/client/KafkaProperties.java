package ru.yandex.practicum.telemetry.aggregator.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;

    private ConsumerConfig consumer = new ConsumerConfig();

    private ProducerConfig producer = new ProducerConfig();

    private TopicsConfig topics = new TopicsConfig();

    @Data
    public static class ConsumerConfig {
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
    }

    @Data
    public static class ProducerConfig {
        private String keySerializer;
        private String valueSerializer;
    }

    @Data
    public static class TopicsConfig {
        private String sensors;
        private String snapshots;
    }
}