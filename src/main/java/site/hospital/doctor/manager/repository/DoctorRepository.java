package site.hospital.doctor.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.doctor.manager.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
