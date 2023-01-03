package site.hospital.dto.review;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.review.ReviewAuthentication;

@Data
public class StaffSearchReviewDTO {

    private Long reviewId;
    private ReviewAuthentication reviewAuthentication;
    private String memberIdName;
    private String nickName;
    private Long likeNumber;

    @QueryProjection
    public StaffSearchReviewDTO(Long reviewId, ReviewAuthentication reviewAuthentication,
            String memberIdName, String nickName, Long likeNumber) {
        this.reviewId = reviewId;
        this.reviewAuthentication = reviewAuthentication;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.likeNumber = likeNumber;
    }
}
