package site.hospital.review.user.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewLikeButtonRequest {

    @NotNull(message = "멤버 번호를 입력해주세요.")
    private Long memberId;
    @NotNull(message = "리뷰 번호를 입력해주세요.")
    private Long reviewId;

}
