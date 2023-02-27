package site.hospital.hospital.user.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalSearchReviewHospitalDTO {

    private Long hospitalId;
    private Double averageRate;
    private Long reviewCount;

    @QueryProjection
    public HospitalSearchReviewHospitalDTO(Long hospitalId, Double averageRate, Long reviewCount) {
        this.hospitalId = hospitalId;
        this.averageRate = averageRate;
        this.reviewCount = reviewCount;
    }
}
