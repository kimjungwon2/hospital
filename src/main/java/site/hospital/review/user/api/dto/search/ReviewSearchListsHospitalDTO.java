package site.hospital.review.user.api.dto.search;

import lombok.Data;
import site.hospital.review.user.domain.reviewhospital.ReviewHospital;

@Data
public class ReviewSearchListsHospitalDTO {

    private String hospitalName;
    private Double averageRate;

    public ReviewSearchListsHospitalDTO(ReviewHospital reviewHospital) {
        this.hospitalName = reviewHospital.getHospital().getHospitalName();
        this.averageRate = reviewHospital.getEvCriteria().getAverageRate();
    }
}
