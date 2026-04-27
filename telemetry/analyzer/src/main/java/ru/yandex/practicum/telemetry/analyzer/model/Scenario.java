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
public class Scenario {
    private Long id;
    private String hubId;
    private String name;
}
