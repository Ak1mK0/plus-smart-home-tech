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
public class Action {
    private Long id;
    private String type;
    private int value;
}
