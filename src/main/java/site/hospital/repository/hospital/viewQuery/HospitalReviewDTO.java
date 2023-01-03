package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalReviewDTO {

    private Long hospitalId;

    @QueryProjection
    public HospitalReviewDTO(Long hospitalId) {
        this.hospitalId = hospitalId;
    }
}
