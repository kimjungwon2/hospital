package site.hospital.review.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewConfirmLikeResponse {

    private final Boolean isReviewLike;

    public static ReviewConfirmLikeResponse from(Boolean isReviewLike) {
        return ReviewConfirmLikeResponse
                .builder()
                .isReviewLike(isReviewLike)
                .build();
    }

}
