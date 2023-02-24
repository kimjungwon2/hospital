package site.hospital.review.user.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewSearchLikeDTO {

    Long reviewLikeId;
    Long reviewId;

    @QueryProjection
    public ReviewSearchLikeDTO(Long reviewLikeId, Long reviewId) {
        this.reviewLikeId = reviewLikeId;
        this.reviewId = reviewId;
    }
}
