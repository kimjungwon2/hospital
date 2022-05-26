package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.hospital.Hospital;

import java.util.List;

public interface HospitalImageRepository extends JpaRepository<HospitalImage, Long> {
    List<HospitalImage> findByHospital(Hospital hospital);
}
