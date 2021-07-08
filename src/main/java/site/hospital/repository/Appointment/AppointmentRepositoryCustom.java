package site.hospital.repository.Appointment;

import site.hospital.domain.appointment.Appointment;

import java.util.List;

public interface AppointmentRepositoryCustom {
    List<Appointment> searchAppointment(Long memberId,Long hospitalId);
}
