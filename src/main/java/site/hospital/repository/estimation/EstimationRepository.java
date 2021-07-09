package site.hospital.repository.estimation;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;

import java.util.List;

public interface EstimationRepository extends JpaRepository<Estimation,Long>,EstimationRepositoryCustom {

}
