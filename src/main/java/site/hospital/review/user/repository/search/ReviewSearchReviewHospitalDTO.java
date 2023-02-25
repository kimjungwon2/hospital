package site.hospital.review.user.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewSearchReviewHospitalDTO {

    private Long reviewId;
    private String content;
    private String disease;
    private Double averageRate;
    private Long hospitalId;
    private String hospitalName;

    @QueryProjection
    public ReviewSearchReviewHospitalDTO(
            Long reviewId,
            String content,
            String disease,
            Double averageRate,
            Long hospitalId,
            String hospitalName
    ) {
        this.reviewId = reviewId;
        this.content = content;
        this.disease = disease;
        this.averageRate = averageRate;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
    }
}
