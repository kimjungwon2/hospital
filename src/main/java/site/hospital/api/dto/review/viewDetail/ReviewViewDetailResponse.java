package site.hospital.api.dto.review.viewDetail;

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
public class ReviewViewDetailResponse {

    private Long reviewId;
    private ReviewAuthentication authenticationStatus;
    private LocalDateTime createdDate;
    private String nickName;
    private List<ReviewViewDetailHospitalDTO> reviewHospitals;
    private Long imageId;
    private String imageKey;


    public static ReviewViewDetailResponse from(Review reviews) {
        if (reviews.getReviewImage() != null) {
            return ReviewViewDetailResponse
                    .builder()
                    .reviewId(reviews.getId())
                    .authenticationStatus(reviews.getAuthenticationStatus())
                    .createdDate(reviews.getCreatedDate())
                    .nickName(reviews.getMember().getNickName())
                    .reviewHospitals(reviews.getReviewHospitals().stream()
                            .map(reviewHospital -> new ReviewViewDetailHospitalDTO(reviewHospital))
                            .collect(Collectors.toList()))
                    .imageId(reviews.getReviewImage().getId())
                    .imageKey(reviews.getReviewImage().getImageKey())
                    .build();
        }

        return ReviewViewDetailResponse
                .builder()
                .reviewId(reviews.getId())
                .authenticationStatus(reviews.getAuthenticationStatus())
                .createdDate(reviews.getCreatedDate())
                .nickName(reviews.getMember().getNickName())
                .reviewHospitals(reviews.getReviewHospitals().stream()
                        .map(reviewHospital -> new ReviewViewDetailHospitalDTO(reviewHospital))
                        .collect(Collectors.toList()))
                .build();
    }
}
