package org.example.appointmentservice.exceptions;

public class DoctorServiceException extends RuntimeException {
    public DoctorServiceException(String message) {
        super(message);
    }
    public DoctorServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
