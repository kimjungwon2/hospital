package site.hospital.repository.reviewLike;

import site.hospital.domain.ReviewLike;

public interface ReviewLikeRepositoryCustom {
    ReviewLike isLikeReview(Long memberId, Long reviewId);
}
