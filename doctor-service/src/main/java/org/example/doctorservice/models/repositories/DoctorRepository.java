package org.example.doctorservice.models.repositories;

import org.example.doctorservice.models.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
