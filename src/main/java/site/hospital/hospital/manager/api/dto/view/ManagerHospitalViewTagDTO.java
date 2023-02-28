package site.hospital.hospital.manager.api.dto.view;

import lombok.Data;
import site.hospital.tag.manager.domain.PostTag;

@Data
public class ManagerHospitalViewTagDTO {

    private Long postTagId;
    private String tagName;

    public ManagerHospitalViewTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
