package site.hospital.api.dto.review.viewLists;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class ReviewViewListsResponse {

    private Long reviewId;
    private ReviewAuthentication authenticationStatus;
    private LocalDateTime createdDate;
    private String nickName;
    private List<ReviewViewListsHospitalDTO> reviewHospitals;
    private List<ReviewViewListsLikeDTO> reviewLikes;

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
