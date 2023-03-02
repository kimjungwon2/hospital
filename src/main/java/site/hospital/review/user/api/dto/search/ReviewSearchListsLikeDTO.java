package site.hospital.review.user.api.dto.search;

import lombok.Data;
import site.hospital.review.user.domain.ReviewLike;

@Data
public class ReviewSearchListsLikeDTO {
    private Long reviewLikeId;

    public ReviewSearchListsLikeDTO(ReviewLike reviewLike) {
        this.reviewLikeId = reviewLike.getId();
    }
}
