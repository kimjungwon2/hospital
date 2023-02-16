package site.hospital.review.manager.api;


import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.review.manager.service.ManagerReviewService;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerReviewController {

    private final ReviewService reviewService;
    private final ManagerReviewService managerReviewService;

    //병원 관계자 리뷰 검색
    @GetMapping("/staff/review/search")
    public Page<ReviewSearchListsResponse> staffSearchReviews(
            ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            Pageable pageable
    ) {
        return managerReviewService.staffSearchReviews(servletRequest, nickName, memberIdName, pageable);
    }

    //병원 관계자 상세보기
    @GetMapping("/staff/review/view/{reviewId}")
    public ReviewViewDetailResponse staffViewReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewHospitalReview(reviewId);
    }

}
