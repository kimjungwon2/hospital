package site.hospital.hospital.user.api.dto.searchver2;

import lombok.Data;
import site.hospital.review.user.domain.reviewHospital.ReviewHospital;

@Data
public class ReviewHospitalVer2DTO {

    private Double averageRate;

    public ReviewHospitalVer2DTO(ReviewHospital reviewHospital) {
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
    }
}
