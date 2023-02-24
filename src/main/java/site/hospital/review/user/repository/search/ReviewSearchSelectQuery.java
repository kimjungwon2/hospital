package site.hospital.review.user.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import site.hospital.review.user.domain.ReviewAuthentication;

@Data
public class ReviewSearchSelectQuery {

    private Long reviewId;
    private String nickName;
    private LocalDateTime createdDate;
    private ReviewAuthentication authenticationStatus;
    private List<ReviewSearchReviewHospitalDTO> reviewHospitals;
    private List<ReviewSearchLikeDTO> reviewLikes;

    @QueryProjection
    public ReviewSearchSelectQuery(
            Long reviewId,
            String nickName,
            LocalDateTime createdDate,
            ReviewAuthentication authenticationStatus
    ) {
        this.reviewId = reviewId;
        this.nickName = nickName;
        this.createdDate = createdDate;
        this.authenticationStatus = authenticationStatus;
    }
}
