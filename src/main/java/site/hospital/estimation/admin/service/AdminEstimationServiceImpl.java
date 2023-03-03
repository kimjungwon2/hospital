package site.hospital.estimation.admin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.estimation.admin.api.dto.EstimationAdminModifyRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateResponse;
import site.hospital.estimation.user.domain.Estimation;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.estimation.admin.repository.EstimationRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminEstimationServiceImpl implements AdminEstimationService{

    private final EstimationRepository estimationRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    @Override
    public EstimationCreateResponse registerHospitalEstimation(EstimationCreateRequest request) {
        Estimation estimation = Estimation
                .builder()
                .cityName(request.getCityName())
                .hospitalName(request.getHospitalName())
                .estimationList(request.getEstimationList())
                .distinctionGrade(request.getDistinctionGrade())
                .build();

        if (request.getHospitalId() == null) {
            return registerEstimation(estimation);
        } else {
            return createHospitalEstimation(request.getHospitalId(), estimation);
        }
    }

    @Transactional
    @Override
    public void deleteEstimation(Long estimationId) {
        estimationRepository.findById(estimationId)
                .orElseThrow(() -> new IllegalStateException("평가가 존재하지 않습니다."));

        estimationRepository.deleteById(estimationId);
    }

    @Transactional
    @Override
    public void modifyEstimation(Long estimationId, EstimationAdminModifyRequest request) {
        Estimation estimation = estimationRepository.findById(estimationId)
                .orElseThrow(() -> new IllegalStateException("평가가 존재하지 않습니다."));

        Estimation modifiedEstimation = Estimation
                .builder()
                .distinctionGrade(request.getDistinctionGrade())
                .estimationList(request.getEstimationList())
                .build();


        estimation.modifyEstimation(modifiedEstimation);
    }

    @Transactional
    protected EstimationCreateResponse createHospitalEstimation(Long hospitalId, Estimation estimation) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 병원은 존재하지 않습니다."));

        checkEmptyEstimationList(estimation, hospital);

        estimation.changeHospital(hospital);

        return registerEstimation(estimation);
    }

    private EstimationCreateResponse registerEstimation(Estimation estimation) {
        estimationRepository.save(estimation);
        return EstimationCreateResponse.from(estimation.getId());
    }

    private void checkEmptyEstimationList(Estimation estimation, Hospital hospital) {
        List<Estimation> estimationList = estimationRepository.searchEstimations(
                hospital, estimation.getEstimationList());

        if (estimationList!= null && !estimationList.isEmpty()) {
            throw new IllegalStateException("이미 등록된 평가 리스트입니다.");
        }
    }

}
