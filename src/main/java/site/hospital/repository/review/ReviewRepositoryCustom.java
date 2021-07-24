package site.hospital.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.review.Review;
import site.hospital.dto.AdminReviewSearchCondition;

import java.util.List;


public interface ReviewRepositoryCustom {
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId, Long reviewId);
    public Page<Review> adminReviews(Pageable pageable);
    public Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable);
}
