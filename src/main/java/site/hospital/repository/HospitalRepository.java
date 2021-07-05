package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital,Long> {
}
