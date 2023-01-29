package site.hospital.api.dto.review.searchReviews;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewSearchListsResponse {

    private final Long reviewId;
    private final ReviewAuthentication reviewAuthentication;
    private final String memberIdName;
    private final String nickName;
    private final List<ReviewSearchListsHospitalDTO> reviewHospitals;
    private final List<ReviewSearchListsLikeDTO> reviewLike;


    public static ReviewSearchListsResponse from(Review review) {
        return ReviewSearchListsResponse
                .builder()
                .reviewId(review.getId())
                .reviewAuthentication(review.getAuthenticationStatus())
                .memberIdName(review.getMember().getMemberIdName())
                .nickName(review.getMember().getNickName())
                .reviewLike(review.getReviewLikes().stream()
                        .map(reviewLike -> new ReviewSearchListsLikeDTO(reviewLike))
                        .collect(Collectors.toList()))
                .reviewHospitals(review.getReviewHospitals().stream()
                        .map(reviewHospital -> new ReviewSearchListsHospitalDTO(reviewHospital))
                        .collect(Collectors.toList()))
                .build();

    }
}
