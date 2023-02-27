package site.hospital.hospital.user.repository.search;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HospitalSearchPostTagDTO {

    private Long hospitalId;
    private Long tagId;
    private String tagName;

    @QueryProjection
    public HospitalSearchPostTagDTO(Long hospitalId, Long tagId, String tagName) {
        this.hospitalId = hospitalId;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
