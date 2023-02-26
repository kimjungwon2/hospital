package site.hospital.estimation.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.estimation.admin.repository.EstimationRepositoryCustom;
import site.hospital.estimation.user.domain.Estimation;

public interface EstimationRepository extends JpaRepository<Estimation, Long>,
        EstimationRepositoryCustom {

}
