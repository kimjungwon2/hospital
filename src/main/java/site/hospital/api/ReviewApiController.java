package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.domain.reviewHospital.EvaluationCriteria;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.service.ReviewService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/hospital/review/{hospitalId}")
    public List<HospitalReviewResponse> searchReview(@PathVariable("hospitalId") Long hospitalId){
        List<Review> review = reviewService.hospitalReviewSearch(hospitalId);

        List<HospitalReviewResponse> result = review.stream().map(r -> new HospitalReviewResponse(r))
                .collect(Collectors.toList());

        return result;
    }

    //리뷰 상세보기
    @GetMapping("/hospital/review/view/{reviewId}")
    public List<ReviewViewResponse> viewReview(@PathVariable("reviewId") Long reviewId){
        List<Review> review = reviewService.hospitalReviewView(reviewId);

        List<ReviewViewResponse> result = review.stream().map(r -> new ReviewViewResponse(r))
                .collect(Collectors.toList());

        return result;
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
        private Long memberId;
        private Long hospitalId;
        private String picture;
        private String content;
        private String disease;
        private Recommendation recommendationStatus;
        private int sumPrice;
        private int kindness;
        private int symptomRelief;
        private int cleanliness;
        private int waitTime;
    }

    @Data
    private static class HospitalReviewResponse{
        private Long reviewId;
        private ReviewAuthentication authenticationStatus;
        private LocalDateTime createdDate;
        private String nickName;
        private List<ReviewHospitalDto> reviewHospitals;


        public HospitalReviewResponse(Review reviews) {
            this.reviewId = reviews.getId();
            this.authenticationStatus = reviews.getAuthenticationStatus();
            this.createdDate = reviews.getCreatedDate();
            this.nickName = reviews.getMember().getNickName();
            this.reviewHospitals = reviews.getReviewHospitals().stream()
                    .map(reviewHospital -> new ReviewHospitalDto(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class ReviewHospitalDto{
        private String content;
        private String disease;
        private Integer likeNumber;

        public ReviewHospitalDto(ReviewHospital reviewHospital) {
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.likeNumber =reviewHospital.getLikeNumber();
        }
    }

    @Data
    private static class ReviewViewResponse{
        private Long reviewId;
        private ReviewAuthentication authenticationStatus;
        private LocalDateTime createdDate;
        private String nickName;
        private List<ReviewViewDto> reviewHospitals;

        public ReviewViewResponse(Review reviews) {
            this.reviewId = reviews.getId();
            this.authenticationStatus = reviews.getAuthenticationStatus();
            this.createdDate = reviews.getCreatedDate();
            this.nickName = reviews.getMember().getNickName();
            this.reviewHospitals = reviews.getReviewHospitals().stream()
                    .map(reviewHospital -> new ReviewViewDto(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class ReviewViewDto{
        private String content;
        private String disease;
        private Integer likeNumber;
        Recommendation recommendationStatus;
        EvaluationCriteria evaluationCriteria;

        public ReviewViewDto(ReviewHospital reviewHospital) {
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.likeNumber =reviewHospital.getLikeNumber();
            this.recommendationStatus = reviewHospital.getRecommendationStatus();
            this.evaluationCriteria = reviewHospital.getEvCriteria();
        }
    }

}
