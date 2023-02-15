package site.hospital.review.user.api;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.review.user.api.dto.ReviewAdminApproveAuthenticationRequest;
import site.hospital.review.user.api.dto.ReviewConfirmLikeResponse;
import site.hospital.review.user.api.dto.ReviewCreateRequest;
import site.hospital.review.user.api.dto.ReviewCreateResponse;
import site.hospital.review.user.api.dto.ReviewLikeButtonRequest;
import site.hospital.review.user.api.dto.member.ReviewViewByMemberResponse;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.api.dto.viewLists.ReviewViewListsResponse;
import site.hospital.review.user.repository.query.ReviewSearchDto;
import site.hospital.review.user.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewApiController {

    private final ReviewService reviewService;

    //리뷰 등록
    @PostMapping("/user/review/register")
    public ReviewCreateResponse saveReview(
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
            ,@ModelAttribute @Validated ReviewCreateRequest requestData
    ) throws IOException {
        return reviewService.reviewRegister(imageFile, requestData);
    }

    //병원에 등록된 리뷰 보기.
    @GetMapping("/hospital/review/{hospitalId}")
    public List<ReviewViewListsResponse> reviewList(@PathVariable("hospitalId") Long hospitalId) {
        return reviewService.hospitalReviewList(hospitalId);
    }

    //유저가 등록한 리뷰 보기
    @GetMapping("/user/{memberId}/reviews")
    public List<ReviewViewByMemberResponse> userReview(@PathVariable("memberId") Long memberId) {
        return reviewService.userReviewSearch(memberId);
    }

    //리뷰 상세보기
    @GetMapping("/review/view/{reviewId}")
    public ReviewViewDetailResponse viewReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewHospitalReview(reviewId);
    }

    //리뷰 좋아요 + 취소하기
    @PostMapping("/user/hospital/review/like")
    public void likeReview(@RequestBody @Validated ReviewLikeButtonRequest request) {
        reviewService.likeReview(request.getMemberId(), request.getReviewId());
    }

    //리뷰 좋아요 여부 확인.
    @GetMapping(value = {"/user/{memberId}/hospital/review/{reviewId}"})
    public ReviewConfirmLikeResponse isNullLike(
            @PathVariable("memberId") Long memberId,
            @PathVariable("reviewId") Long reviewId
    ) {
        return reviewService.isLikeReview(memberId, reviewId);
    }

    //리뷰 검색하기
    @GetMapping("/search/review/{searchName}")
    public Page<ReviewSearchDto> searchReview(
            @PathVariable("searchName") String searchName,
            Pageable pageable
    ) {
        return reviewService.searchReview(searchName, pageable);
    }

    //병원 관계자 리뷰 검색
    @GetMapping("/staff/review/search")
    public Page<ReviewSearchListsResponse> staffSearchReviews(
            ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return reviewService.staffSearchReviews(servletRequest, nickName, memberIdName, pageable);
    }

    //병원 관계자 상세보기
    @GetMapping("/staff/review/view/{reviewId}")
    public ReviewViewDetailResponse staffViewReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewHospitalReview(reviewId);
    }

    //관리자 리뷰 검색
    @GetMapping("/admin/review/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return reviewService.adminSearchReviews(nickName, hospitalName, memberIdName, pageable);
    }

    //관리자 미승인 리뷰 갯수
    @GetMapping("/admin/review/unapproved/count")
    public Long adminUnapprovedReviewCount() {
        return reviewService.adminUnapprovedReviewCount();
    }

    //관리자 미승인 리뷰 검색
    @GetMapping("/admin/review/unapproved/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(Pageable pageable) {
        return reviewService.adminSearchUnapprovedReviews(pageable);
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
        return reviewService.viewHospitalReview(reviewId);
    }

}
