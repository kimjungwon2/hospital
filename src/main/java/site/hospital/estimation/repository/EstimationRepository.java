package site.hospital.estimation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.estimation.domain.Estimation;

public interface EstimationRepository extends JpaRepository<Estimation, Long>,
        EstimationRepositoryCustom {

}
