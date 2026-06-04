package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;
    @Column(name = "total_payment")
    private double totalPayment;
    @Column(name = "total_delivery")
    private double deliveryTotal;
    @Column(name = "total_fee")
    private double feeTotal;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
