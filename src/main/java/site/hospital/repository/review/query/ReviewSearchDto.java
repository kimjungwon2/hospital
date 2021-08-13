package site.hospital.repository.review.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.review.ReviewAuthentication;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewSearchDto {
    private Long reviewId;
    private String nickName;
    private LocalDateTime createdDate;
    private ReviewAuthentication authenticationStatus;
    private List<ReviewHospitalDTO2> reviewHospitals;
    private List<ReviewLikeSearchDTO> reviewLikes;

    @QueryProjection
    public ReviewSearchDto(Long reviewId, String nickName, LocalDateTime createdDate, ReviewAuthentication authenticationStatus) {
        this.reviewId = reviewId;
        this.nickName = nickName;
        this.createdDate = createdDate;
        this.authenticationStatus = authenticationStatus;
    }
}
