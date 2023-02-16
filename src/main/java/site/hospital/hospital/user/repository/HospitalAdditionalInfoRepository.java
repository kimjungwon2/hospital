package site.hospital.hospital.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.StaffHosInformation;

public interface HospitalAdditionalInfoRepository extends JpaRepository<StaffHosInformation, Long> {

}
