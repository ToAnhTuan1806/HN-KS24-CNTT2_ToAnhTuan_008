package org.example.doctorservice.models.services;

import org.example.doctorservice.models.entities.Doctor;

public interface DoctorService {
    Doctor getDoctorById(Long id);
}
