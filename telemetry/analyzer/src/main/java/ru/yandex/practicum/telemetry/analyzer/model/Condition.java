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
public class Condition {
    private Long id;
    private String type;
    private String operation;
    private int value;
}
