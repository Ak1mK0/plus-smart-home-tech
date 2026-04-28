package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sensors")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "hub_id", nullable = false)
    private String hubId;
}
