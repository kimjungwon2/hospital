package site.hospital.review.user.repository.reviewlike;

import site.hospital.review.user.domain.ReviewLike;

public interface ReviewLikeRepositoryCustom {

    ReviewLike checkReviewLike(Long memberId, Long reviewId);
}
