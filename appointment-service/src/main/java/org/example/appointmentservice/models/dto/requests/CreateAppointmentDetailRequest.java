package org.example.appointmentservice.models.dto.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAppointmentDetailRequest(
        @NotNull(message = "Doctor ID không được để trống")
        Long doctorId,

        @NotNull(message = "Số lượt khám không được để trống")
        @Positive(message = "Số lượt khám phải lớn hơn 0")
        Integer slots
) {
}
