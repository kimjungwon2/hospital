package site.hospital.review.user.api.dto.detail;

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
public class ReviewViewDetailResponse {

    private final Long reviewId;
    private final ReviewAuthentication authenticationStatus;
    private final LocalDateTime createdDate;
    private final String nickName;
    private final List<ReviewViewDetailHospitalDTO> reviewHospitals;
    private final Long imageId;
    private final String imageKey;

    private ReviewViewDetailResponse(Review review) {
        if (review.getReviewImage() != null) {
            Assert.notNull(review.getReviewImage(),"reviewImage must be provided");

            this.reviewId = review.getId();
            this.authenticationStatus = review.getAuthenticationStatus();
            this.createdDate = review.getCreatedDate();
            this.nickName = review.getMember().getNickName();

            this.imageId = review.getReviewImage().getId();
            this.imageKey = review.getReviewImage().getImageKey();

            this.reviewHospitals = review
                    .getReviewHospitals()
                    .stream()
                    .map(reviewHospital -> new ReviewViewDetailHospitalDTO(reviewHospital))
                    .collect(Collectors.toList());
        }
        else{
            this.reviewId = review.getId();
            this.authenticationStatus = review.getAuthenticationStatus();
            this.createdDate = review.getCreatedDate();
            this.nickName = review.getMember().getNickName();

            this.imageId = null;
            this.imageKey = null;

            this.reviewHospitals = review
                    .getReviewHospitals()
                    .stream()
                    .map(reviewHospital -> new ReviewViewDetailHospitalDTO(reviewHospital))
                    .collect(Collectors.toList());
        }
    }

    public static ReviewViewDetailResponse from(Review review) {
        Assert.notNull(review,"review must be provided");

        return new ReviewViewDetailResponse(review);
    }
}
