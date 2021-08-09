package site.hospital.repository.estimation;

import site.hospital.domain.estimation.Estimation;
import site.hospital.domain.estimation.EstimationList;
import site.hospital.domain.hospital.Hospital;

import java.util.List;

public interface EstimationRepositoryCustom {
    List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList);
    void adminDeleteEstimation(Hospital hospital);
}
