package site.hospital.hospital.user.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.HospitalImage;
import site.hospital.hospital.user.domain.Hospital;

public interface HospitalImageRepository extends JpaRepository<HospitalImage, Long> {

    List<HospitalImage> findByHospital(Hospital hospital);
}
