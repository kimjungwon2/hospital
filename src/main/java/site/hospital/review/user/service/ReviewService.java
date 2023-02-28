package site.hospital.review.user.service;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.review.user.api.dto.ReviewConfirmLikeResponse;
import site.hospital.review.user.api.dto.ReviewCreateRequest;
import site.hospital.review.user.api.dto.ReviewCreateResponse;
import site.hospital.review.user.api.dto.member.ReviewViewByMemberResponse;
import site.hospital.review.user.api.dto.detail.ReviewViewDetailResponse;
import site.hospital.review.user.api.dto.view.ReviewViewListsResponse;
import site.hospital.review.user.repository.search.ReviewSearchSelectQuery;

public interface ReviewService {
    ReviewCreateResponse registerReview(
            MultipartFile imageFile,
            ReviewCreateRequest requestData
    ) throws IOException;
    ReviewConfirmLikeResponse checkReviewLike(Long memberId, Long reviewId);
    void likeReview(Long memberId, Long reviewId);
    List<ReviewViewListsResponse> viewHospitalReviews(Long hospitalId);
    List<ReviewViewByMemberResponse> viewReviewsByUser(Long memberId);
    Page<ReviewSearchSelectQuery> searchReviews(String searchName, Pageable pageable);
    ReviewViewDetailResponse viewDetailedReview(Long reviewId);

}
