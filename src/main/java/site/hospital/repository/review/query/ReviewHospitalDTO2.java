package site.hospital.repository.review.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewHospitalDTO2 {

    private Long reviewId;
    private String content;
    private String disease;
    private Integer likeNumber;
    private Double averageRate;
    private String hospitalName;

    @QueryProjection
    public ReviewHospitalDTO2(Long reviewId, String content, String disease,
                              Integer likeNumber, Double averageRate, String hospitalName) {
        this.reviewId = reviewId;
        this.content = content;
        this.disease = disease;
        this.likeNumber = likeNumber;
        this.averageRate = averageRate;
        this.hospitalName = hospitalName;
    }
}
