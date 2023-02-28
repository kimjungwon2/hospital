package site.hospital.review.user.api.dto.member;

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
public class ReviewViewByMemberResponse {

    private final Long reviewId;
    private final ReviewAuthentication authenticationStatus;
    private final LocalDateTime createdDate;
    private final List<ReviewViewByMemberUserDTO> reviewHospitals;

    private ReviewViewByMemberResponse(Review review) {
        this.reviewId = review.getId();
        this.authenticationStatus = review.getAuthenticationStatus();
        this.createdDate = review.getCreatedDate();
        this.reviewHospitals = review
                .getReviewHospitals()
                .stream()
                .map(reviewHospital -> new ReviewViewByMemberUserDTO(reviewHospital))
                .collect(Collectors.toList());
    }

    public static ReviewViewByMemberResponse from(Review review) {
        Assert.notNull(review,"review must be provided");

        return new ReviewViewByMemberResponse(review);
    }
}
