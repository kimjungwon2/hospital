package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.estimation.EstimationAdminModifyRequest;
import site.hospital.api.dto.estimation.EstimationCreateRequest;
import site.hospital.api.dto.estimation.EstimationCreateResponse;
import site.hospital.domain.estimation.Estimation;
import site.hospital.service.EstimationService;

@RestController
@RequiredArgsConstructor
public class EstimationApiController {

    private final EstimationService estimationService;

    //평가 등록
    @PostMapping("/admin/estimation/register")
    public EstimationCreateResponse createEstimation(
            @RequestBody @Validated EstimationCreateRequest request) {
        Estimation estimation = Estimation.builder().cityName(request.getCityName())
                .hospitalName(request.getHospitalName()).estimationList(request.getEstimationList())
                .distinctionGrade(request.getDistinctionGrade()).build();

        //병원 아이디를 기입 안 할 경우.
        if (request.getHospitalId() == null) {
            Long estimationId = estimationService.createNoHospitalEstimation(estimation);

            return EstimationCreateResponse.from(estimationId);
        } else {
            Long id = estimationService
                    .createHospitalEstimation(request.getHospitalId(), estimation);
            return EstimationCreateResponse.from(id);
        }
    }

    //평가 삭제
    @DeleteMapping("/admin/estimation/delete/{estimationId}")
    public void adminDeleteEstimation(@PathVariable("estimationId") Long estimationId) {
        estimationService.adminDeleteEstimation(estimationId);
    }

    //관리자 평가 수정하기
    @PutMapping("/admin/estimation/modify/{estimationId}")
    public void adminModifyMember(@PathVariable("estimationId") Long estimationId,
            @RequestBody @Validated EstimationAdminModifyRequest request) {
        Estimation estimation = Estimation.builder().distinctionGrade(request.getDistinctionGrade())
                .estimationList(request.getEstimationList()).build();

        estimationService.adminModifyEstimation(estimationId, estimation);
    }

}
