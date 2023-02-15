package site.hospital.review.user.api.dto.viewLists;

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
public class ReviewViewListsResponse {

    private final Long reviewId;
    private final ReviewAuthentication authenticationStatus;
    private final LocalDateTime createdDate;
    private final String nickName;
    private final List<ReviewViewListsHospitalDTO> reviewHospitals;
    private final List<ReviewViewListsLikeDTO> reviewLikes;

    public static ReviewViewListsResponse from(Review reviews) {
        return ReviewViewListsResponse
                .builder()
                .reviewId(reviews.getId())
                .authenticationStatus(reviews.getAuthenticationStatus())
                .createdDate(reviews.getCreatedDate())
                .nickName(reviews.getMember().getNickName())
                .reviewHospitals(reviews.getReviewHospitals().stream()
                        .map(reviewHospital -> new ReviewViewListsHospitalDTO(reviewHospital))
                        .collect(Collectors.toList()))
                .reviewLikes(reviews.getReviewLikes().stream()
                        .map(reviewLike -> new ReviewViewListsLikeDTO(reviewLike))
                        .collect(Collectors.toList()))
                .build();
    }

}
