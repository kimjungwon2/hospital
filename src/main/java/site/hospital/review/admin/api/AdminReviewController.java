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

    @GetMapping("/admin/review/search")
    public Page<ReviewSearchListsResponse> adminSearchReviews(
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "hospitalName", required = false) String hospitalName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return adminReviewService.searchReviews(nickName, hospitalName, memberIdName, pageable);
    }

    @GetMapping("/admin/review/unapproved/count")
    public Long adminCountUnapprovedReviews() {
        return adminReviewService.countUnapprovedReviews();
    }

    @GetMapping("/admin/review/unapproved/search")
    public Page<ReviewSearchListsResponse> adminSearchUnapprovedReviews(Pageable pageable) {
        return adminReviewService.searchUnapprovedReviews(pageable);
    }

    @PutMapping("/admin/review/approve/{reviewId}")
    public void adminApproveReview(
            @PathVariable("reviewId") Long reviewId,
            @RequestBody @Validated ReviewAdminApproveAuthenticationRequest request
    ) {
        adminReviewService.approveReview(reviewId, request.getReviewAuthentication());
    }

    @DeleteMapping("/admin/review/delete/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long reviewId) {
        adminReviewService.deleteReview(reviewId);
    }

    @GetMapping("/admin/review/view/{reviewId}")
    public ReviewViewDetailResponse adminViewDetailedReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewDetailedReview(reviewId);
    }

}
