package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
