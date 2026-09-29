package org.example.appointmentservice.models.services;

import org.example.appointmentservice.models.dto.requests.CreateAppointmentRequest;
import org.example.appointmentservice.models.dto.responses.AppointmentResponse;

public interface AppointmentService {
    AppointmentResponse createAppointment(CreateAppointmentRequest request);
}
