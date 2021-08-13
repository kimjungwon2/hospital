package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.HospitalImage;

public interface HospitalImageRepository extends JpaRepository<HospitalImage, Long> {
}
