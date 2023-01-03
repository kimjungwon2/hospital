package site.hospital.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.HospitalImage;
import site.hospital.domain.hospital.Hospital;

public interface HospitalImageRepository extends JpaRepository<HospitalImage, Long> {

    List<HospitalImage> findByHospital(Hospital hospital);
}
