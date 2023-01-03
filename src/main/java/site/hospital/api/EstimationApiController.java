package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.estimation.Estimation;
import site.hospital.domain.estimation.EstimationList;
import site.hospital.service.EstimationService;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
public class EstimationApiController {

    private final EstimationService estimationService;

    //평가 등록
    @PostMapping("/admin/estimation/register")
    public CreateEstimationResponse createEstimation(
            @RequestBody @Validated CreateReviewRequest request) {
        Estimation estimation = Estimation.builder().cityName(request.getCityName())
                .hospitalName(request.getHospitalName()).estimationList(request.getEstimationList())
                .distinctionGrade(request.getDistinctionGrade()).build();

        //병원 아이디를 기입 안 할 경우.
        if (request.getHospitalId() == null) {
            Long estimationId = estimationService.createNoHospitalEstimation(estimation);

            return new CreateEstimationResponse(estimationId);
        } else {
            Long id = estimationService
                    .createHospitalEstimation(request.getHospitalId(), estimation);
            return new CreateEstimationResponse(id);
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
            @RequestBody @Validated AdminModifyEstimationRequest request) {
        Estimation estimation = Estimation.builder().distinctionGrade(request.getDistinctionGrade())
                .
                        estimationList(request.getEstimationList()).build();

        estimationService.adminModifyEstimation(estimationId, estimation);
    }



    /* DTO */

    @Data
    private static class CreateEstimationResponse {

        long estimationId;

        public CreateEstimationResponse(long estimationId) {
            this.estimationId = estimationId;
        }
    }

    @Data
    private static class CreateReviewRequest {

        @NotNull(message = "병원 번호를 입력해주세요.")
        private Long hospitalId;
        @NotNull(message = "도시 이름을 입력해주세요.")
        private String cityName;
        @NotNull(message = "병원 이름을 입력해주세요.")
        private String hospitalName;
        @NotNull(message = "등급을 입력해주세요.")
        private String distinctionGrade;
        @NotNull(message = "평가 내역을 입력해주세요.")
        private EstimationList estimationList;
    }

    @Data
    private static class AdminModifyEstimationRequest {

        private String distinctionGrade;
        private EstimationList estimationList;
    }

}
