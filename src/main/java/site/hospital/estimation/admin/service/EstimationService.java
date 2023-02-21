package site.hospital.estimation.admin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.estimation.admin.api.dto.EstimationAdminModifyRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateRequest;
import site.hospital.estimation.admin.api.dto.EstimationCreateResponse;
import site.hospital.estimation.admin.domain.Estimation;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.estimation.admin.repository.EstimationRepository;
import site.hospital.hospital.user.repository.HospitalRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EstimationService {

    private final EstimationRepository estimationRepository;
    private final HospitalRepository hospitalRepository;

    //병원 FK 없이 등록
    @Transactional
    public EstimationCreateResponse createNoHospitalEstimation(EstimationCreateRequest request) {
        Estimation estimation = Estimation.builder().cityName(request.getCityName())
                .hospitalName(request.getHospitalName()).estimationList(request.getEstimationList())
                .distinctionGrade(request.getDistinctionGrade()).build();

        //병원 아이디를 기입 안 할 경우.
        if (request.getHospitalId() == null) {
            estimationRepository.save(estimation);
            return EstimationCreateResponse.from(estimation.getId());
        } else {
            createHospitalEstimation(request.getHospitalId(), estimation);
            return EstimationCreateResponse.from(estimation.getId());
        }

    }

    //병원 FK 있을 때, 등록.
    @Transactional
    private Long createHospitalEstimation(Long hospitalId, Estimation estimation) {

        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 병원은 존재하지 않습니다."));

        List<Estimation> estimationList = estimationRepository.searchEstimation(hospital,
                estimation.getEstimationList());

        // 이미 중복된 평가 리스트인지 확인한다.
        if (estimationList!= null && !estimationList.isEmpty()) {
            throw new IllegalStateException("이미 등록된 평가 리스트입니다.");
        }

        estimation.changeHospital(hospital);
        estimationRepository.save(estimation);

        return estimation.getId();
    }

    //평가 삭제하기.
    @Transactional
    public void adminDeleteEstimation(Long estimationId) {
        Estimation estimation = estimationRepository.findById(estimationId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 평가가 존재하지 않습니다."));

        estimationRepository.deleteById(estimationId);
    }

    //평가 수정하기
    @Transactional
    public void adminModifyEstimation(Long estimationId, EstimationAdminModifyRequest request) {
        Estimation estimation = Estimation.builder().distinctionGrade(request.getDistinctionGrade())
                .estimationList(request.getEstimationList()).build();

        Estimation findEstimation = estimationRepository.findById(estimationId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 평가가 존재하지 않습니다."));

        findEstimation.modifyEstimation(estimation);
    }

}
