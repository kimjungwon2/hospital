package site.hospital.review.user.api.dto.search;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;

@Data
public class ReviewSearchListsResponse {

    private final Long reviewId;
    private final ReviewAuthentication reviewAuthentication;
    private final String memberIdName;
    private final String nickName;
    private final List<ReviewSearchListsHospitalDTO> reviewHospitals;
    private final List<ReviewSearchListsLikeDTO> reviewLike;

    private ReviewSearchListsResponse(Review review) {
        Assert.notNull(review.getMember(),"member must be provided");

        this.reviewId = review.getId();
        this.reviewAuthentication = review.getAuthenticationStatus();
        this.memberIdName = review.getMember().getMemberIdName();
        this.nickName = review.getMember().getNickName();

        this.reviewHospitals = review
                .getReviewHospitals()
                .stream()
                .map(reviewHospital -> new ReviewSearchListsHospitalDTO(reviewHospital))
                .collect(Collectors.toList());

        this.reviewLike = review
                .getReviewLikes()
                .stream()
                .map(reviewLike -> new ReviewSearchListsLikeDTO(reviewLike))
                .collect(Collectors.toList());
    }

    public static ReviewSearchListsResponse from(Review review) {
        Assert.notNull(review,"review must be provided");

        return new ReviewSearchListsResponse(review);
    }
}
