package org.example.appointmentservice.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AppointmentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "slots", nullable = false)
    private Integer slots;

    @Column(name = "consultation_fee", nullable = false)
    private Double consultationFee;

    @Column(name = "line_total", nullable = false)
    private Double lineTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;
}