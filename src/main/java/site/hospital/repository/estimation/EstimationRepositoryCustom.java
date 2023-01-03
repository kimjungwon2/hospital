package site.hospital.repository.estimation;

import java.util.List;
import site.hospital.domain.estimation.Estimation;
import site.hospital.domain.estimation.EstimationList;
import site.hospital.domain.hospital.Hospital;

public interface EstimationRepositoryCustom {

    List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList);

    void adminDeleteEstimation(Hospital hospital);
}
