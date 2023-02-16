package site.hospital.estimation.admin.repository;

import java.util.List;
import site.hospital.estimation.admin.domain.Estimation;
import site.hospital.estimation.admin.domain.EstimationList;
import site.hospital.hospital.user.domain.Hospital;

public interface EstimationRepositoryCustom {

    List<Estimation> searchEstimation(Hospital hospital, EstimationList estimationList);

    void adminDeleteEstimation(Hospital hospital);
}