package site.hospital.review.user.api.dto.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.util.Assert;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewViewListsResponse {

    private final Long reviewId;
    private final ReviewAuthentication authenticationStatus;
    private final LocalDateTime createdDate;
    private final String nickName;
    private final List<ReviewViewListsHospitalDTO> reviewHospitals;
    private final List<ReviewViewListsLikeDTO> reviewLikes;

    private ReviewViewListsResponse(Review review) {
        this.reviewId = review.getId();
        this.authenticationStatus = review.getAuthenticationStatus();
        this.createdDate = review.getCreatedDate();

        Assert.notNull(review.getMember(),"member must be provided");
        this.nickName = review.getMember().getNickName();

        this.reviewHospitals = review
                .getReviewHospitals()
                .stream()
                .map(reviewHospital -> new ReviewViewListsHospitalDTO(reviewHospital))
                .collect(Collectors.toList());

        this.reviewLikes = review
                .getReviewLikes()
                .stream()
                .map(reviewLike -> new ReviewViewListsLikeDTO(reviewLike))
                .collect(Collectors.toList());
    }

    public static ReviewViewListsResponse from(Review review) {
        Assert.notNull(review,"review must be provided");

        return new ReviewViewListsResponse(review);
    }

}
