package site.hospital.repository.estimation;

import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;

import java.util.List;

public interface EstimationRepositoryCustom {
    List<Estimation> searchEstimation(Hospital hospital, String estimationList);
    void adminDeleteEstimation(Hospital hospital);
}
