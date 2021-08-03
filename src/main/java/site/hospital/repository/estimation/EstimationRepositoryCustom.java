package site.hospital.repository.estimation;

import site.hospital.domain.Estimation;
import site.hospital.domain.Hospital;

import java.util.List;

public interface EstimationRepositoryCustom {
    List<Estimation> searchEstimation(String hospitalName, String cityName);
    void adminDeleteEstimation(Hospital hospital);
}
