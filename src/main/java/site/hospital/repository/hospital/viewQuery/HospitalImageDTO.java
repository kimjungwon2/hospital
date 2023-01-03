package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalImageDTO {

    private Long hospitalId;
    private Long hospitalImageId;
    private String imageKey;

    @QueryProjection
    public HospitalImageDTO(Long hospitalId, Long hospitalImageId,
            String imageKey) {
        this.hospitalImageId = hospitalImageId;
        this.imageKey = imageKey;
        this.hospitalId = hospitalId;
    }
}
