package site.hospital.hospital.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.HospitalThumbnail;
import site.hospital.hospital.user.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.hospital.user.domain.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long>,
        HospitalRepositoryCustom {

    Hospital findByDetailedHosInformation(DetailedHosInformation detailedHosInformation);

    Hospital findByHospitalThumbnail(HospitalThumbnail hospitalThumbnail);
}
