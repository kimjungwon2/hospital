package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalReviewDTO {
    private Long hospitalId;
    private Long reviewCount;

    @QueryProjection
    public HospitalReviewDTO(Long hospitalId, Long reviewCount) {
        this.hospitalId = hospitalId;
        this.reviewCount = reviewCount;
    }
}
