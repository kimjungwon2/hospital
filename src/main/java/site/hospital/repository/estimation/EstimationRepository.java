package site.hospital.repository.estimation;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.estimation.Estimation;

public interface EstimationRepository extends JpaRepository<Estimation, Long>,
        EstimationRepositoryCustom {

}
