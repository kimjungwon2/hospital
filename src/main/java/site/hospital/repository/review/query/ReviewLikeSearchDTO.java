package site.hospital.repository.review.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewLikeSearchDTO {
    Long reviewLikeId;
    Long reviewId;

    @QueryProjection
    public ReviewLikeSearchDTO(Long reviewLikeId, Long reviewId) {
        this.reviewLikeId = reviewLikeId;
        this.reviewId = reviewId;
    }
}
