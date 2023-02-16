package site.hospital.review.user.repository.reviewLike;

import site.hospital.review.user.domain.ReviewLike;

public interface ReviewLikeRepositoryCustom {

    ReviewLike isLikeReview(Long memberId, Long reviewId);
}
