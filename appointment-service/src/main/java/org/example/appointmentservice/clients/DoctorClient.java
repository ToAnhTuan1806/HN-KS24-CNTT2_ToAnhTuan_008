package org.example.appointmentservice.clients;

import org.example.appointmentservice.models.dto.responses.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctor-service", path = "/api/v1/doctors")
public interface DoctorClient {

    @GetMapping("/{id}")
    DoctorResponse getDoctorById(@PathVariable("id") Long id);

}

