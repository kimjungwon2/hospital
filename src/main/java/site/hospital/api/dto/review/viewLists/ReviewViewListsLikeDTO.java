package site.hospital.api.dto.review.viewLists;

import lombok.Data;
import site.hospital.domain.ReviewLike;

@Data
public class ReviewViewListsLikeDTO {

    Long likeId;
    Long memberId;

    public ReviewViewListsLikeDTO(ReviewLike reviewLike) {
        this.likeId = reviewLike.getId();
        this.memberId = reviewLike.getMember().getId();
    }
}
