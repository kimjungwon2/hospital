package site.hospital.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.api.dto.review.ReviewAdminApproveAuthenticationRequest;
import site.hospital.api.dto.review.ReviewConfirmLikeResponse;
import site.hospital.api.dto.review.ReviewCreateRequest;
import site.hospital.api.dto.review.ReviewCreateResponse;
import site.hospital.api.dto.review.ReviewLikeButtonRequest;
import site.hospital.api.dto.review.member.ReviewViewByMemberResponse;
import site.hospital.api.dto.review.searchReviews.ReviewSearchListsResponse;
import site.hospital.api.dto.review.viewDetail.ReviewViewDetailResponse;
import site.hospital.api.dto.review.viewLists.ReviewViewListsResponse;
import site.hospital.domain.ReviewLike;
import site.hospital.domain.review.Review;
import site.hospital.domain.reviewHospital.EvaluationCriteria;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;
import site.hospital.repository.review.query.ReviewSearchDto;
import site.hospital.service.ImageManagementService;
import site.hospital.service.ReviewService;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;
    private final ImageManagementService imageManagementService;

    //리뷰 등록
    @PostMapping("/user/review/register")
    public ReviewCreateResponse saveReview(
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
            , @ModelAttribute @Validated ReviewCreateRequest requestData) throws IOException {
        EvaluationCriteria evaluationCriteria = EvaluationCriteria.builder()
                .sumPrice(requestData.getSumPrice()).kindness(requestData.getKindness())
                .symptomRelief(requestData.getSymptomRelief())
                .cleanliness(requestData.getCleanliness())
                .waitTime(requestData.getWaitTime()).build();

        ReviewHospital reviewHospital = ReviewHospital.builder()
                .content(requestData.getContent()).disease(requestData.getDisease())
                .recommendationStatus(requestData.getRecommendationStatus())
                .evCriteria(evaluationCriteria)
                .build();

        Long reviewId = reviewService
                .reviewRegister(requestData.getMemberId(), requestData.getHospitalId(),
                        reviewHospital);

        //영수증 파일 저장.
        if (imageFile != null) {
            imageManagementService.reviewReceiptUpload(imageFile, "receipt", reviewId);
        }

        return ReviewCreateResponse.from(reviewId);
    }

    //병원에 등록된 리뷰 보기.
    @GetMapping("/hospital/review/{hospitalId}")
    public List<ReviewViewListsResponse> reviewList(@PathVariable("hospitalId") Long hospitalId) {
        List<Review> review = reviewService.hospitalReviewList(hospitalId);

        List<ReviewViewListsResponse> result = review.stream()
                .map(r -> ReviewViewListsResponse.from(r))
                .collect(Collectors.toList());

        return result;
    }

    //유저가 등록한 리뷰 보기
    @GetMapping("/user/{memberId}/reviews")
    public List<ReviewViewByMemberResponse> userReview(@PathVariable("memberId") Long memberId) {
        List<Review> review = reviewService.userReviewSearch(memberId);

        List<ReviewViewByMemberResponse> result = review.stream()
                .map(r -> ReviewViewByMemberResponse.from(r))
                .collect(Collectors.toList());

        return result;
    }

    //리뷰 상세보기
    @GetMapping("/review/view/{reviewId}")
    public ReviewViewDetailResponse viewReview(@PathVariable("reviewId") Long reviewId) {

        Review review = reviewService.viewHospitalReview(reviewId);

        ReviewViewDetailResponse result = ReviewViewDetailResponse.from(review);

        return result;
    }

    //리뷰 좋아요 + 취소하기
    @PostMapping("/user/hospital/review/like")
    public void likeReview(@RequestBody @Validated ReviewLikeButtonRequest request) {
        reviewService.likeReview(request.getMemberId(), request.getReviewId());
    }

    //리뷰 좋아요 여부 확인.
    @GetMapping(value = {"/user/{memberId}/hospital/review/{reviewId}"})
    public ReviewConfirmLikeResponse isNullLike(@PathVariable("memberId") Long memberId,
            @PathVariable("reviewId") Long reviewId) {
        Boolean isLikeReview = false;
        ReviewLike reviewLike = reviewService.isLikeReview(memberId, reviewId);

        //좋아요가 있으면 true 반환
        if (reviewLike != null) {
            isLikeReview = true;
        }

        return ReviewConfirmLikeResponse.from(isLikeReview);
    }

    //리뷰 검색하기
    @GetMapping("/search/review/{searchName}")
    public Page<ReviewSearchDto> searchReview(@PathVariable("searchName") String searchName,
            Pageable pageable) {
        return reviewService.searchReview(searchName, pageable);
    }

    //병원 관계자 리뷰 검색
    @GetMapping("/staff/review/search")
    public Page<ReviewSearchListsResponse> staffSearchReviews(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable) {
        //받은 값들 생성자로 생성.
        StaffReviewSearchCondition condition = StaffReviewSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Page<Review> reviews = reviewService
                .staffSearchReviews(servletRequest, condition, pageable);
        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //병원 관계자 상세보기
    @GetMapping("/staff/review/view/{reviewId}")
    public ReviewViewDetailResponse staffViewReview(@PathVariable("reviewId") Long reviewId) {

        Review review = reviewService.viewHospitalReview(reviewId);
        ReviewViewDetailResponse result = ReviewViewDetailResponse.from(review);

        return result;
    }

    //관리자 리뷰 검색
    @GetMapping("/admin/review/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable) {
        //받은 값들 생성자로 생성.
        AdminReviewSearchCondition condition = AdminReviewSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();

        Page<Review> reviews = reviewService.adminSearchReviews(condition, pageable);
        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //관리자 미승인 리뷰 갯수
    @GetMapping("/admin/review/unapproved/count")
    public Long adminUnapprovedReviewCount() {
        Long unapprovedCount = reviewService.adminUnapprovedReviewCount();

        return unapprovedCount;
    }

    //관리자 미승인 리뷰 검색
    @GetMapping("/admin/review/unapproved/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(Pageable pageable) {

        Page<Review> reviews = reviewService.adminSearchUnapprovedReviews(pageable);
        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //관리자 리뷰 승인해주기
    @PutMapping("/admin/review/approve/{reviewId}")
    public void approveReview(@PathVariable("reviewId") Long reviewId,
            @RequestBody @Validated ReviewAdminApproveAuthenticationRequest request) {
        reviewService.approve(reviewId, request.getReviewAuthentication());
    }

    //관리자 리뷰 삭제
    @DeleteMapping("/admin/review/delete/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    //관리자 리뷰 상세보기
    @GetMapping("/admin/review/view/{reviewId}")
    public ReviewViewDetailResponse adminViewReview(@PathVariable("reviewId") Long reviewId) {

        Review review = reviewService.viewHospitalReview(reviewId);
        ReviewViewDetailResponse result = ReviewViewDetailResponse.from(review);

        return result;
    }

}
