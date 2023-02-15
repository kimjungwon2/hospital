package site.hospital.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.doctor.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
