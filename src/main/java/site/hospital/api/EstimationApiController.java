package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.estimation.EstimationAdminModifyRequest;
import site.hospital.api.dto.estimation.EstimationCreateRequest;
import site.hospital.api.dto.estimation.EstimationCreateResponse;
import site.hospital.service.EstimationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class EstimationApiController {

    private final EstimationService estimationService;

    //평가 등록
    @PostMapping("/admin/estimation/register")
    public EstimationCreateResponse createEstimation(
            @RequestBody @Validated EstimationCreateRequest request) {
        return estimationService.createNoHospitalEstimation(request);
    }

    //평가 삭제
    @DeleteMapping("/admin/estimation/delete/{estimationId}")
    public void adminDeleteEstimation(@PathVariable("estimationId") Long estimationId) {
        estimationService.adminDeleteEstimation(estimationId);
    }

    //관리자 평가 수정하기
    @PutMapping("/admin/estimation/modify/{estimationId}")
    public void adminModifyMember(
            @PathVariable("estimationId") Long estimationId,
            @RequestBody @Validated EstimationAdminModifyRequest request
    ) {
        estimationService.adminModifyEstimation(estimationId, request);
    }

}
