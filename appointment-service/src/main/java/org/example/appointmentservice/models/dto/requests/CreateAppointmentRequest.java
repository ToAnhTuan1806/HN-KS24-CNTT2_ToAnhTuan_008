package org.example.appointmentservice.models.dto.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateAppointmentRequest(
        @NotBlank(message = "Tên bệnh nhân không được để trống")
        String patientName,

        @NotBlank(message = "Email bệnh nhân không được để trống")
        @Email(message = "Email bệnh nhân không hợp lệ")
        String patientEmail,

        @NotEmpty(message = "Danh sách bác sĩ không được để trống")
        List<@Valid CreateAppointmentDetailRequest> items
) {
}
