package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;

public interface DetailedHosRepository extends JpaRepository<DetailedHosInformation,Long> {
}
