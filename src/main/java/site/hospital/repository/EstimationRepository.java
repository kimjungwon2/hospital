package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Estimation;

public interface EstimationRepository extends JpaRepository<Estimation,Long> {
}
