package org.example.appointmentservice.models.dto.responses;

import org.example.appointmentservice.models.constants.AppointmentStatus;

import java.util.List;

public record AppointmentResponse(
        Long id,

        String patientName,

        String patientEmail,

        Double totalAmount,

        AppointmentStatus status,

        List<AppointmentDetailResponse> details
) {
}
