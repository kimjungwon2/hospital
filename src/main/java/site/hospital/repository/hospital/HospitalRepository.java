package site.hospital.repository.hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.HospitalThumbnail;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.hospital.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long>,
        HospitalRepositoryCustom {

    Hospital findByDetailedHosInformation(DetailedHosInformation detailedHosInformation);

    Hospital findByHospitalThumbnail(HospitalThumbnail hospitalThumbnail);
}
