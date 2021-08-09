package site.hospital.repository.hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;

public interface HospitalRepository extends JpaRepository<Hospital,Long>, HospitalRepositoryCustom{
   Hospital findByDetailedHosInformation(DetailedHosInformation detailedHosInformation);
}
