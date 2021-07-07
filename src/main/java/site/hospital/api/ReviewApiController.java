package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.service.ReviewService;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {
    private final ReviewService reviewService;

    @PostMapping("/hospital/review")
    public CreateReviewResponse saveReview(@RequestBody @Validated CreateReviewRequest request){

        Long id= reviewService.reviewRegister(request.getMemberId(),request.getHospitalId(),
                request.getPicture(),request.getContent(),request.getDisease(),
                request.getRecommendationStatus(),request.getSumPrice(),request.getKindness(),
                request.getSymptomRelief(),request.getCleanliness(),request.getWaitTime());

        return new CreateReviewResponse(id);
    }


    /* DTO */
    @Data
    private static class CreateReviewResponse {
        long id;
        public CreateReviewResponse(long id) {
            this.id = id;
        }
    }
    @Data
    private static class CreateReviewRequest {
        Long memberId;
        Long hospitalId;
        String picture;
        String content;
        String disease;
        Recommendation recommendationStatus;
        int sumPrice;
        int kindness;
        int symptomRelief;
        int cleanliness;
        int waitTime;
    }

}
