package ru.yandex.practicum.telemetry.analyzer.client;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public class KafkaClientConfigurationImpl<T extends SpecificRecordBase> implements ClientConfiguration<T> {
    private final KafkaProperties kafkaProperties;
    private Consumer<String, T> consumer;


    public Consumer<String, T> getConsumer(String groupId) {
        if (consumer == null) {
            initConsumer(groupId);
        }
        return consumer;
    }

    public void stop() {
        if (consumer != null) {
            consumer.close();
        }
    }

    private void initConsumer(String groupId) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        consumer = new KafkaConsumer<>(config);
    }
}
