package site.hospital.hospital.user.repository.view;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalViewTagDTO {

    private Long hospitalId;
    private String tagName;

    @QueryProjection
    public HospitalViewTagDTO(Long hospitalId, String tagName) {
        this.hospitalId = hospitalId;
        this.tagName = tagName;
    }
}
