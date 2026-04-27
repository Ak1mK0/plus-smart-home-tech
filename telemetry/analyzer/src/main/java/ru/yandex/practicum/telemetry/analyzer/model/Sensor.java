package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {
    private String id;
    private String hubId;
}
