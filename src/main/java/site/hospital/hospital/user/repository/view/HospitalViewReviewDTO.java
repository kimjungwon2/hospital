package site.hospital.hospital.user.repository.view;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalViewReviewDTO {

    private Long hospitalId;

    @QueryProjection
    public HospitalViewReviewDTO(Long hospitalId) {
        this.hospitalId = hospitalId;
    }
}
