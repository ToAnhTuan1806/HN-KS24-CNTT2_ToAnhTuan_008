package org.example.appointmentservice.models.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.appointmentservice.exceptions.DoctorServiceException;
import org.example.appointmentservice.models.constants.AppointmentStatus;
import org.example.appointmentservice.models.dto.requests.CreateAppointmentRequest;
import org.example.appointmentservice.models.dto.responses.AppointmentDetailResponse;
import org.example.appointmentservice.models.dto.responses.AppointmentResponse;
import org.example.appointmentservice.models.dto.responses.DoctorResponse;
import org.example.appointmentservice.models.entities.Appointment;
import org.example.appointmentservice.models.entities.AppointmentDetail;
import org.example.appointmentservice.models.repositories.AppointmentDetailRepository;
import org.example.appointmentservice.models.repositories.AppointmentRepository;
import org.example.appointmentservice.models.services.AppointmentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentDetailRepository appointmentDetailRepository;
    private final DoctorGatewayService doctorGatewayService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private record PreparedDetail(Long doctorId, Integer slots, Double consultationFee, Double lineTotal) {
    }

    @Override
    @Transactional
    public AppointmentResponse createAppointment(CreateAppointmentRequest request) {
        // Duyệt toàn bộ item, gọi Doctor Service lấy thông tin mới nhất
        List<PreparedDetail> prepared = new ArrayList<>();
        double totalAmount = 0d;

        for (var item : request.items()) {
            DoctorResponse doctor = doctorGatewayService.getDoctorById(item.doctorId());

            if (doctor.consultationFee() == null) {
                throw new DoctorServiceException("Invalid doctor data for ID: " + item.doctorId());
            }
            if (doctor.availableSlots() == null) {
                throw new DoctorServiceException("Invalid doctor data for ID: " + item.doctorId());
            }

            Integer slots = item.slots();
            if (slots == null || slots <= 0) {
                throw new IllegalArgumentException("Số lượt khám phải lớn hơn 0 (doctorId=" + item.doctorId() + ")");
            }
            if (slots > doctor.availableSlots()) {
                throw new IllegalArgumentException(
                        "Not enough available slots for doctor ID: " + item.doctorId()
                                + ". Requested: " + slots + ", available: " + doctor.availableSlots());
            }

            double lineTotal = doctor.consultationFee() * slots;
            totalAmount += lineTotal;
            prepared.add(new PreparedDetail(doctor.id(), slots, doctor.consultationFee(), lineTotal));
        }

        // Tạo và lưu Appointment với trạng thái PENDING
        Appointment appointment = Appointment.builder()
                .patientName(request.patientName())
                .patientEmail(request.patientEmail())
                .totalAmount(totalAmount)
                .status(AppointmentStatus.PENDING)
                .build();
        Appointment saved = appointmentRepository.save(appointment);

        // Tạo, liên kết và lưu đầy đủ các AppointmentDetail
        List<AppointmentDetail> savedDetails = new ArrayList<>();
        for (PreparedDetail p : prepared) {
            AppointmentDetail detail = AppointmentDetail.builder()
                    .doctorId(p.doctorId())
                    .slots(p.slots())
                    .consultationFee(p.consultationFee())
                    .lineTotal(p.lineTotal())
                    .appointment(saved)
                    .build();
            savedDetails.add(appointmentDetailRepository.save(detail));
        }

        // Chỉ phát sự kiện sau khi lưu thành công
        kafkaTemplate.send("appointment-created", request.patientEmail());

        List<AppointmentDetailResponse> detailResponses = savedDetails.stream()
                .map(d -> new AppointmentDetailResponse(
                        d.getId(),
                        d.getDoctorId(),
                        d.getSlots(),
                        d.getConsultationFee(),
                        d.getLineTotal()))
                .toList();

        return new AppointmentResponse(
                saved.getId(),
                saved.getPatientName(),
                saved.getPatientEmail(),
                saved.getTotalAmount(),
                saved.getStatus(),
                detailResponses);
    }
}
