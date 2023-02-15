package site.hospital.estimation.repository;

import java.util.List;
import site.hospital.estimation.domain.Estimation;
import site.hospital.estimation.domain.EstimationList;
import site.hospital.hospital.user.domain.Hospital;

public interface EstimationRepositoryCustom {

    List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList);

    void adminDeleteEstimation(Hospital hospital);
}
