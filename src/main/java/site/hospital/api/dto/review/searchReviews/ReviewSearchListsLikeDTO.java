package site.hospital.api.dto.review.searchReviews;

import lombok.Data;
import site.hospital.domain.ReviewLike;

@Data
public class ReviewSearchListsLikeDTO {
    private Long reviewLikeId;

    public ReviewSearchListsLikeDTO(ReviewLike ReviewLike) {
        this.reviewLikeId = ReviewLike.getId();
    }
}
