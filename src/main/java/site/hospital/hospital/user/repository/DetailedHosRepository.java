package site.hospital.hospital.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.detailedHosInformation.DetailedHosInformation;

public interface DetailedHosRepository extends JpaRepository<DetailedHosInformation, Long> {

}
