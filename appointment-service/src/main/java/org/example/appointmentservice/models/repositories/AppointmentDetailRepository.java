package org.example.appointmentservice.models.repositories;

import org.example.appointmentservice.models.entities.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentDetailRepository extends JpaRepository<AppointmentDetail, Long> {
}
