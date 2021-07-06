package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
