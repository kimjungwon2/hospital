package site.hospital.repository.hospital.viewQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalTagDTO {
    private Long hospitalId;
    private String tagName;

    @QueryProjection
    public HospitalTagDTO(Long hospitalId, String tagName) {
        this.hospitalId = hospitalId;
        this.tagName = tagName;
    }
}
