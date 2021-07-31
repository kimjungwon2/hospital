package site.hospital.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.review.Review;
import site.hospital.dto.AdminReviewSearchCondition;

import java.util.List;


public interface ReviewRepositoryCustom {
    List<Review> hospitalReviewSearch(Long hospitalId, Long memberId);
    Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable);
    Review viewHospitalReview(Long reviewId);
}
