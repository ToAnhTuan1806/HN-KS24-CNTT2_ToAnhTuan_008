package org.example.appointmentservice.models.dto.responses;

public record DoctorResponse(
        Long id,

        String doctorName,

        String specialization,

        Double consultationFee,

        Integer availableSlots
) {
}
