package org.example.appointmentservice.exceptions;

public class DoctorNotFoundException extends RuntimeException {
    public DoctorNotFoundException(Long id) {
        super("Doctor not found with ID: " + id);
    }
}
