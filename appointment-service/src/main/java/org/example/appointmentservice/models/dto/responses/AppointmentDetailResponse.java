package org.example.appointmentservice.models.dto.responses;

public record AppointmentDetailResponse(
        Long id,

        Long doctorId,

        Integer slots,

        Double consultationFee,

        Double lineTotal
) {
}
