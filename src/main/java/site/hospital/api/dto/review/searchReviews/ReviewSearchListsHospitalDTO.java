package site.hospital.api.dto.review.searchReviews;

import lombok.Data;
import site.hospital.domain.reviewHospital.ReviewHospital;

@Data
public class ReviewSearchListsHospitalDTO {

    private String hospitalName;
    private Double averageRate;

    public ReviewSearchListsHospitalDTO(ReviewHospital reviewHospital) {
        this.hospitalName = reviewHospital.getHospital().getHospitalName();
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
    }
}
