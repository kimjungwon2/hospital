package site.hospital.repository.review;

import site.hospital.domain.review.Review;

import java.util.List;


public interface ReviewRepositoryCustom {
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId, Long reviewId);
}
