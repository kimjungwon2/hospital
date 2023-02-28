package site.hospital.review.user.api.dto.view;

import lombok.Data;
import site.hospital.review.user.domain.ReviewLike;

@Data
public class ReviewViewListsLikeDTO {

    Long likeId;
    Long memberId;

    public ReviewViewListsLikeDTO(ReviewLike reviewLike) {
        this.likeId = reviewLike.getId();
        this.memberId = reviewLike.getMember().getId();
    }
}
