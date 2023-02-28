package site.hospital.estimation.admin.service;

import site.hospital.estimation.admin.api.dto.EstimationAdminModifyRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateResponse;

public interface AdminEstimationService {

    EstimationCreateResponse registerHospitalEstimation(EstimationCreateRequest request);

    void deleteEstimation(Long estimationId);

    void modifyEstimation(Long estimationId, EstimationAdminModifyRequest request);

}
