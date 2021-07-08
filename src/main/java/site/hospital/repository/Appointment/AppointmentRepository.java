package site.hospital.repository.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.appointment.Appointment;



public interface AppointmentRepository extends JpaRepository<Appointment, Long> ,AppointmentRepositoryCustom{

}
