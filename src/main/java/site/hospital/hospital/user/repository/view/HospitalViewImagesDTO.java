package site.hospital.hospital.user.repository.view;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalViewImagesDTO {

    private Long hospitalId;
    private Long hospitalImageId;
    private String imageKey;

    @QueryProjection
    public HospitalViewImagesDTO(Long hospitalId, Long hospitalImageId,
            String imageKey) {
        this.hospitalImageId = hospitalImageId;
        this.imageKey = imageKey;
        this.hospitalId = hospitalId;
    }
}
