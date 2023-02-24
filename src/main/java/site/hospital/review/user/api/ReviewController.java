package site.hospital.review.user.api;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.review.user.api.dto.ReviewConfirmLikeResponse;
import site.hospital.review.user.api.dto.ReviewCreateRequest;
import site.hospital.review.user.api.dto.ReviewCreateResponse;
import site.hospital.review.user.api.dto.ReviewLikeButtonRequest;
import site.hospital.review.user.api.dto.member.ReviewViewByMemberResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.api.dto.viewLists.ReviewViewListsResponse;
import site.hospital.review.user.repository.search.ReviewSearchSelectQuery;
import site.hospital.review.user.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/user/review/register")
    public ReviewCreateResponse registerReview(
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
            ,@ModelAttribute @Validated ReviewCreateRequest requestData
    ) throws IOException {
        return reviewService.registerReview(imageFile, requestData);
    }

    @GetMapping("/hospital/review/{hospitalId}")
    public List<ReviewViewListsResponse> viewHospitalReviews(@PathVariable("hospitalId") Long hospitalId) {
        return reviewService.viewHospitalReviews(hospitalId);
    }

    @GetMapping("/user/{memberId}/reviews")
    public List<ReviewViewByMemberResponse> viewReviewsByUser(@PathVariable("memberId") Long memberId) {
        return reviewService.viewReviewsByUser(memberId);
    }

    @GetMapping("/review/view/{reviewId}")
    public ReviewViewDetailResponse viewDetailedReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.viewDetailedReview(reviewId);
    }

    @PostMapping("/user/hospital/review/like")
    public void likeReview(@RequestBody @Validated ReviewLikeButtonRequest request) {
        reviewService.likeReview(request.getMemberId(), request.getReviewId());
    }

    @GetMapping(value = {"/user/{memberId}/hospital/review/{reviewId}"})
    public ReviewConfirmLikeResponse checkReviewLike(
            @PathVariable("memberId") Long memberId,
            @PathVariable("reviewId") Long reviewId
    ) {
        return reviewService.checkReviewLike(memberId, reviewId);
    }

    @GetMapping("/search/review/{searchName}")
    public Page<ReviewSearchSelectQuery> searchReviews(
            @PathVariable("searchName") String searchName,
            Pageable pageable
    ) {
        return reviewService.searchReviews(searchName, pageable);
    }

}
