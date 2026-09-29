package org.example.appointmentservice.models.services.impl;

import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.example.appointmentservice.clients.DoctorClient;
import org.example.appointmentservice.exceptions.DoctorNotFoundException;
import org.example.appointmentservice.exceptions.DoctorServiceException;
import org.example.appointmentservice.models.dto.responses.DoctorResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorGatewayService {
    private final DoctorClient doctorClient;

    @CircuitBreaker(name = "doctorService", fallbackMethod = "getDoctorByIdFallback")
    public DoctorResponse getDoctorById(Long doctorId) {
        try {
            DoctorResponse response = doctorClient.getDoctorById(doctorId);
            if (response == null || response.id() == null) {
                throw new DoctorNotFoundException(doctorId);
            }
            return response;
        } catch (DoctorNotFoundException e) {
            throw e;
        } catch (FeignException.NotFound e) {
            throw new DoctorNotFoundException(doctorId);
        } catch (FeignException e) {
            throw new DoctorServiceException("Doctor service unavailable: " + e.getMessage(), e);
        }
    }

    public DoctorResponse getDoctorByIdFallback(Long doctorId, Throwable t) {
        // DoctorNotFoundException ném ra sẽ không bị fallback (Resilience4j propagates certain exceptions)
        // Xử lý trường hợp lỗi service không khả dụng
        Throwable cause = t;
        while (cause != null) {
            if (cause instanceof DoctorNotFoundException notFound) {
                throw notFound;
            }
            if (cause instanceof FeignException.NotFound) {
                throw new DoctorNotFoundException(doctorId);
            }
            cause = cause.getCause();
        }
        throw new DoctorServiceException("Doctor service is currently unavailable, please try again later", t);
    }
}
