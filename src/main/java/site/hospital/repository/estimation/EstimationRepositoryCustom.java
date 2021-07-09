package site.hospital.repository.estimation;

import site.hospital.domain.Estimation;

import java.util.List;

public interface EstimationRepositoryCustom {
    public List<Estimation> searchEstimation(String hospitalName, String cityName);
}
