package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.domain.reviewHospital.EvaluationCriteria;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.domain.ReviewLike;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;
import site.hospital.dto.review.StaffSearchReviewDTO;
import site.hospital.repository.review.query.ReviewSearchDto;
import site.hospital.service.ReviewService;

import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {
    private final ReviewService reviewService;

    //리뷰 등록
    @PostMapping("/user/review/register")
    public CreateReviewResponse saveReview(@RequestBody @Validated CreateReviewRequest request){
        EvaluationCriteria evaluationCriteria = EvaluationCriteria.builder()
                .sumPrice(request.getSumPrice()).kindness(request.getKindness())
                .symptomRelief(request.getSymptomRelief()).cleanliness(request.getCleanliness())
                .waitTime(request.getWaitTime()).build();

        ReviewHospital reviewHospital = ReviewHospital.builder()
                .content(request.getContent()).disease(request.getDisease())
                .recommendationStatus(request.getRecommendationStatus()).evCriteria(evaluationCriteria)
                .build();

        Long id= reviewService.reviewRegister(request.getMemberId(),request.getHospitalId(),
                reviewHospital);

        return new CreateReviewResponse(id);
    }

    //병원에 등록된 리뷰 보기.
    @GetMapping("/hospital/review/{hospitalId}")
    public List<HospitalReviewResponse> reviewList(@PathVariable("hospitalId") Long hospitalId){
        List<Review> review = reviewService.hospitalReviewList(hospitalId);

        List<HospitalReviewResponse> result = review.stream().map(r -> new HospitalReviewResponse(r))
                .collect(Collectors.toList());

        return result;
    }

    //유저가 등록한 리뷰 보기
    @GetMapping("/user/{memberId}/reviews")
    public List<UserReviewResponse> userReview(@PathVariable("memberId") Long memberId){
        List<Review> review = reviewService.userReviewSearch(memberId);

        List<UserReviewResponse> result = review.stream().map(r -> new UserReviewResponse(r))
                .collect(Collectors.toList());

        return result;
    }

    //리뷰 상세보기
    @GetMapping("/review/view/{reviewId}")
    public ReviewViewResponse viewReview(@PathVariable("reviewId") Long reviewId){

        Review review = reviewService.viewHospitalReview(reviewId);

        ReviewViewResponse result = new ReviewViewResponse(review);

        return result;
    }

    //리뷰 좋아요 + 취소하기
    @PostMapping("/user/hospital/review/like")
    public void likeReview(@RequestBody @Validated ReviewLikeRequest request){
        reviewService.likeReview(request.getMemberId(), request.getReviewId());
    }

    //리뷰 좋아요 여부 확인.
    @GetMapping(value ={"/user/{memberId}/hospital/review/{reviewId}"})
    public IsLikeReview isNullLike(@PathVariable("memberId") Long memberId,
                                   @PathVariable("reviewId") Long reviewId){
        Boolean isLikeReview = false;
        ReviewLike reviewLike = reviewService.isLikeReview(memberId, reviewId);

        //좋아요가 있으면 true 반환
        if(reviewLike !=null) isLikeReview =true;

        return new IsLikeReview(isLikeReview);
    }

    //리뷰 검색하기
    @GetMapping("/search/review/{searchName}")
    public Page<ReviewSearchDto> searchReview(@PathVariable("searchName") String searchName, Pageable pageable){
        return reviewService.searchReview(searchName, pageable);
    }

    //관리자 리뷰 검색
    @GetMapping("/staff/review/search")
    public Page<AdminReviewsResponse> staffSearchReviews(ServletRequest servletRequest, @RequestParam(value="nickName",required = false) String nickName,
                                                         @RequestParam(value="memberIdName",required = false) String memberIdName,
                                                         Pageable pageable){
        //받은 값들 생성자로 생성.
        StaffReviewSearchCondition condition = StaffReviewSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Page<Review> reviews = reviewService.staffSearchReviews(servletRequest, condition, pageable);
        List<AdminReviewsResponse> result = reviews.stream().map(r->new AdminReviewsResponse(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //관리자 리뷰 검색
    @GetMapping("/admin/review/search")
    public Page<AdminReviewsResponse> adminSearchReviews(@RequestParam(value="nickName",required = false) String nickName,
                                                         @RequestParam(value="hospitalName",required = false) String hospitalName,
                                                         @RequestParam(value="memberIdName",required = false) String memberIdName,
                                                         Pageable pageable){
        //받은 값들 생성자로 생성.
        AdminReviewSearchCondition condition = AdminReviewSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();

        Page<Review> reviews = reviewService.adminSearchReviews(condition,pageable);
        List<AdminReviewsResponse> result = reviews.stream().map(r->new AdminReviewsResponse(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //관리자 리뷰 승인해주기
    @PutMapping("/admin/review/approve/{reviewId}")
    public void approveReview(@PathVariable("reviewId") Long reviewId,@RequestBody @Validated AdminApproveReviewRequest request){
        reviewService.approve(reviewId,request.getReviewAuthentication());
    }

    //관리자 리뷰 삭제
    @DeleteMapping("/admin/review/delete/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId){
        reviewService.deleteReview(reviewId);
    }

    //관리자 리뷰 상세보기
    @GetMapping("/admin/review/view/{reviewId}")
    public ReviewViewResponse adminViewReview(@PathVariable("reviewId") Long reviewId){

        Review review = reviewService.viewHospitalReview(reviewId);
        ReviewViewResponse result = new ReviewViewResponse(review);

        return result;
    }


    /* DTO */
    @Data
    private static class CreateReviewResponse {
        Long id;
        public CreateReviewResponse(long id) {
            this.id = id;
        }
    }
    @Data
    private static class CreateReviewRequest {
        private Long memberId;
        private Long hospitalId;
        private String content;
        private String disease;
        private Recommendation recommendationStatus;
        private Integer sumPrice;
        private Integer kindness;
        private Integer symptomRelief;
        private Integer cleanliness;
        private Integer waitTime;
    }

    @Data
    private static class HospitalReviewResponse{
        private Long reviewId;
        private ReviewAuthentication authenticationStatus;
        private LocalDateTime createdDate;
        private String nickName;
        private List<ReviewHospitalDto> reviewHospitals;
        private List<ReviewLikeDTO> reviewLikes;


        public HospitalReviewResponse(Review reviews) {
            this.reviewId = reviews.getId();
            this.authenticationStatus = reviews.getAuthenticationStatus();
            this.createdDate = reviews.getCreatedDate();
            this.nickName = reviews.getMember().getNickName();
            this.reviewHospitals = reviews.getReviewHospitals().stream()
                    .map(reviewHospital -> new ReviewHospitalDto(reviewHospital))
                    .collect(Collectors.toList());
            this.reviewLikes = reviews.getReviewLikes().stream()
                    .map(reviewLike -> new ReviewLikeDTO(reviewLike))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class ReviewLikeDTO{
        Long likeId;
        Long memberId;

        public ReviewLikeDTO(ReviewLike reviewLike) {
            this.likeId = reviewLike.getId();
            this.memberId = reviewLike.getMember().getId();
        }
    }

    @Data
    private static class ReviewHospitalDto{
        private String content;
        private String disease;
        private Integer sumPrice;
        private Integer kindness;
        private Integer symptomRelief;
        private Integer cleanliness;
        private Integer waitTime;
        private Double averageRate;

        public ReviewHospitalDto(ReviewHospital reviewHospital) {
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.sumPrice = reviewHospital.getEvCriteria().getSumPrice();
            this.kindness = reviewHospital.getEvCriteria().getKindness();
            this.symptomRelief = reviewHospital.getEvCriteria().getSymptomRelief();
            this.cleanliness = reviewHospital.getEvCriteria().getCleanliness();
            this.waitTime = reviewHospital.getEvCriteria().getWaitTime();
            this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
        }
    }

    @Data
    private static class ReviewLikeRequest{
        private Long memberId;
        private Long reviewId;
    }

    @Data
    private static class UserReviewResponse{
        private Long reviewId;
        private ReviewAuthentication authenticationStatus;
        private LocalDateTime createdDate;
        private List<ReviewHospitalUserDto> reviewHospitals;


        public UserReviewResponse(Review reviews) {
            this.reviewId = reviews.getId();
            this.authenticationStatus = reviews.getAuthenticationStatus();
            this.createdDate = reviews.getCreatedDate();
            this.reviewHospitals = reviews.getReviewHospitals().stream()
                    .map(reviewHospital -> new ReviewHospitalUserDto(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class ReviewHospitalUserDto{
        private Long hospitalId;
        private String hospitalName;
        private String content;
        private String disease;
        private Recommendation recommendationStatus;
        private Integer sumPrice;
        private Integer kindness;
        private Integer symptomRelief;
        private Integer cleanliness;
        private Integer waitTime;
        private Double averageRate;

        public ReviewHospitalUserDto(ReviewHospital reviewHospital) {
            this.hospitalId = reviewHospital.getHospital().getId();
            this.hospitalName = reviewHospital.getHospital().getHospitalName();
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.recommendationStatus = reviewHospital.getRecommendationStatus();
            this.sumPrice = reviewHospital.getEvCriteria().getSumPrice();
            this.kindness = reviewHospital.getEvCriteria().getKindness();
            this.symptomRelief = reviewHospital.getEvCriteria().getSymptomRelief();
            this.cleanliness = reviewHospital.getEvCriteria().getCleanliness();
            this.waitTime = reviewHospital.getEvCriteria().getWaitTime();
            this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
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
    private static class AdminApproveReviewRequest{
        private ReviewAuthentication reviewAuthentication;
    }

    @Data
    private static class ReviewViewDto{
        private String content;
        private String disease;
        private String hospitalName;
        private String medicalSubjectInformation;
        Recommendation recommendationStatus;
        EvaluationCriteria evaluationCriteria;

        public ReviewViewDto(ReviewHospital reviewHospital) {
            this.hospitalName = reviewHospital.getHospital().getHospitalName();
            this.medicalSubjectInformation =reviewHospital.getHospital().getMedicalSubjectInformation();
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.recommendationStatus = reviewHospital.getRecommendationStatus();
            this.evaluationCriteria = reviewHospital.getEvCriteria();
        }
    }

    @Data
    private static class AdminReviewView{
        private Long reviewId;
        private Long memberId;
        private ReviewAuthentication authenticationStatus;
        private LocalDateTime createdDate;
        private String userName;
        private String nickName;
        private List<AdminReviewDto> reviewHospitals;

        public AdminReviewView(Review reviews) {
            this.reviewId = reviews.getId();
            this.memberId = reviews.getMember().getId();
            this.userName = reviews.getMember().getUserName();
            this.authenticationStatus = reviews.getAuthenticationStatus();
            this.createdDate = reviews.getCreatedDate();
            this.nickName = reviews.getMember().getNickName();
            this.reviewHospitals = reviews.getReviewHospitals().stream()
                    .map(reviewHospital -> new AdminReviewDto(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class IsLikeReview{
        private Boolean isReviewLike;

        public IsLikeReview(Boolean isReviewLike) {
            this.isReviewLike = isReviewLike;
        }
    }

    @Data
    private static class AdminReviewDto{
        private Long reviewHospitalId;
        private Long hospitalId;
        private String content;
        private String disease;
        private String hospitalName;
        private String hospitalCityName;
        private String medicalSubject;
        Recommendation recommendationStatus;
        EvaluationCriteria evaluationCriteria;

        public AdminReviewDto(ReviewHospital reviewHospital) {
            this.reviewHospitalId = reviewHospital.getId();
            this.hospitalId = reviewHospital.getHospital().getId();
            this.hospitalName = reviewHospital.getHospital().getHospitalName();
            this.hospitalCityName = reviewHospital.getHospital().getCityName();
            this.medicalSubject =reviewHospital.getHospital().getMedicalSubjectInformation();
            this.content = reviewHospital.getContent();
            this.disease = reviewHospital.getDisease();
            this.recommendationStatus = reviewHospital.getRecommendationStatus();
            this.evaluationCriteria = reviewHospital.getEvCriteria();
        }
    }

    @Data
    private static class AdminReviewsResponse {
        private Long reviewId;
        private ReviewAuthentication reviewAuthentication;
        private String memberIdName;
        private String nickName;
        private List<AdminReviewsHospitalDto> reviewHospitals;
        private List<AdminReviewLikesDto> reviewLike;


        public AdminReviewsResponse(Review review) {
            this.reviewId = review.getId();
            this.reviewAuthentication = review.getAuthenticationStatus();
            this.memberIdName = review.getMember().getMemberIdName();
            this.nickName = review.getMember().getNickName();
            this.reviewLike = review.getReviewLikes().stream()
                    .map(reviewLike -> new AdminReviewLikesDto(reviewLike))
                    .collect(Collectors.toList());
            this.reviewHospitals = review.getReviewHospitals().stream()
                    .map(reviewHospital -> new AdminReviewsHospitalDto(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    @Data
    private static class AdminReviewLikesDto{
        private Long reviewLikeId;

        public AdminReviewLikesDto(ReviewLike ReviewLike) {
            this.reviewLikeId = ReviewLike.getId();
        }
    }

    @Data
    private static class AdminReviewsHospitalDto{
        private String hospitalName;
        private Double averageRate;

        public AdminReviewsHospitalDto(ReviewHospital reviewHospital) {
            this.hospitalName = reviewHospital.getHospital().getHospitalName();
            this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
        }
    }
}
