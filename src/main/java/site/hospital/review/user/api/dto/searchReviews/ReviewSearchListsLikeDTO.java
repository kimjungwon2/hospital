package site.hospital.review.user.api.dto.searchReviews;

import lombok.Data;
import site.hospital.review.user.domain.ReviewLike;

@Data
public class ReviewSearchListsLikeDTO {
    private Long reviewLikeId;

    public ReviewSearchListsLikeDTO(ReviewLike ReviewLike) {
        this.reviewLikeId = ReviewLike.getId();
    }
}
