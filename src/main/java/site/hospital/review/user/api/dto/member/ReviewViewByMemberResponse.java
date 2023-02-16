package site.hospital.review.user.api.dto.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewViewByMemberResponse {

    private final Long reviewId;
    private final ReviewAuthentication authenticationStatus;
    private final LocalDateTime createdDate;
    private final List<ReviewViewByMemberUserDTO> reviewHospitals;

    public static ReviewViewByMemberResponse from(Review reviews) {
        return ReviewViewByMemberResponse
                .builder()
                .reviewId(reviews.getId())
                .authenticationStatus(reviews.getAuthenticationStatus())
                .createdDate(reviews.getCreatedDate())
                .reviewHospitals(reviews.getReviewHospitals().stream()
                        .map(reviewHospital -> new ReviewViewByMemberUserDTO(reviewHospital))
                        .collect(Collectors.toList()))
                .build();
    }
}