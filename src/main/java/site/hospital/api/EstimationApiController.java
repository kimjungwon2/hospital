package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.Estimation;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.service.EstimationService;

@RestController
@RequiredArgsConstructor
public class EstimationApiController {

    private final EstimationService estimationService;

    //평가 등록
    @PostMapping("/admin/estimation/register")
    public CreateEstimationResponse createEstimation(@RequestBody @Validated CreateReviewRequest request){
        Estimation estimation = Estimation.builder().cityName(request.getCityName())
                .hospitalName(request.getHospitalName()).estimationList(request.getEstimationList())
                .distinctionGrade(request.getDistinctionGrade()).build();

        //병원 아이디를 기입 안 할 경우.
        if (request.getHospitalId() == null){
            Long id = estimationService.createEstimation(estimation);

            return new CreateEstimationResponse(id);
        }

        else {
            Long id = estimationService.createHospitalEstimation(request.getHospitalId(), estimation);

            return new CreateEstimationResponse(id);
        }
    }



    /* DTO */

    @Data
    private static class CreateEstimationResponse {
        long estimationId;
        public CreateEstimationResponse(long estimationId){
            this.estimationId = estimationId;
        }
    }

    @Data
    private static class CreateReviewRequest {
        private Long hospitalId;
        private String cityName;
        private String hospitalName;
        private String distinctionGrade;
        private String estimationList;
    }

}
