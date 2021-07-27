package site.hospital.repository.review.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewHospitalDTO2 {

    private Long reviewId;
    private String content;
    private String disease;
    private Double averageRate;
    private String hospitalName;

    @QueryProjection
    public ReviewHospitalDTO2(Long reviewId, String content, String disease,
                              Double averageRate, String hospitalName) {
        this.reviewId = reviewId;
        this.content = content;
        this.disease = disease;
        this.averageRate = averageRate;
        this.hospitalName = hospitalName;
    }
}
