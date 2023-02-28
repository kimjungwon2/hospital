package site.hospital.review.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.review.user.api.dto.search.ReviewSearchListsResponse;
import site.hospital.review.user.domain.ReviewAuthentication;

public interface AdminReviewService {

    void deleteReview(Long reviewId);

    void approveReview(Long reviewId, ReviewAuthentication reviewAuthentication);

    Page<ReviewSearchListsResponse> searchUnapprovedReviews(Pageable pageable);

    Long countUnapprovedReviews();

    Page<ReviewSearchListsResponse> searchReviews(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    );
}
