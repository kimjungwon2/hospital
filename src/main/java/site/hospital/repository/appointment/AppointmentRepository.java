package site.hospital.repository.appointment;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.appointment.Appointment;



public interface AppointmentRepository extends JpaRepository<Appointment, Long> ,AppointmentRepositoryCustom{

}
