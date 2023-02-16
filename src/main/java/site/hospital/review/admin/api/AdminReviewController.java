package site.hospital.review.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.review.admin.service.AdminReviewService;
import site.hospital.review.user.api.dto.ReviewAdminApproveAuthenticationRequest;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminReviewController {

    private final ReviewService reviewService;
    private final AdminReviewService adminReviewService;

    //관리자 리뷰 검색
    @GetMapping("/admin/review/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return adminReviewService.adminSearchReviews(nickName, hospitalName, memberIdName, pageable);
    }

    //관리자 미승인 리뷰 갯수
    @GetMapping("/admin/review/unapproved/count")
    public Long adminUnapprovedReviewCount() {
        return adminReviewService.adminUnapprovedReviewCount();
    }

    //관리자 미승인 리뷰 검색
    @GetMapping("/admin/review/unapproved/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(Pageable pageable) {
        return adminReviewService.adminSearchUnapprovedReviews(pageable);
    }

    //관리자 리뷰 승인해주기
    @PutMapping("/admin/review/approve/{reviewId}")
    public void approveReview(@PathVariable("reviewId") Long reviewId,
            @RequestBody @Validated ReviewAdminApproveAuthenticationRequest request) {
        adminReviewService.approve(reviewId, request.getReviewAuthentication());
    }

    //관리자 리뷰 삭제
    @DeleteMapping("/admin/review/delete/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        adminReviewService.deleteReview(reviewId);
    }

    //관리자 리뷰 상세보기
    @GetMapping("/admin/review/view/{reviewId}")
    public ReviewViewDetailResponse adminViewReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewHospitalReview(reviewId);
    }

}
