package ru.yandex.practicum.telemetry.analyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.yandex.practicum.telemetry.analyzer.service.SnapshotProcessor;

@SpringBootApplication
@ComponentScan
public class Analyzer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Analyzer.class, args);

        SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);
        snapshotProcessor.start();
    }
}
