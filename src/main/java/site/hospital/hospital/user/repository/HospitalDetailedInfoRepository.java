package site.hospital.hospital.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.detailedinfo.DetailedHosInformation;

public interface HospitalDetailedInfoRepository extends JpaRepository<DetailedHosInformation, Long> {

}
