package site.hospital.repository.review;

import site.hospital.domain.review.Review;

import java.util.List;


public interface ReviewRepositoryCustom {
    List<Review> hospitalReviewSearch(Long hospitalId, Long reviewId);
}
