package site.hospital.api.dto.review;

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
