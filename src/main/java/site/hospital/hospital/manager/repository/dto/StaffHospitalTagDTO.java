package site.hospital.hospital.manager.repository.dto;

import lombok.Data;
import site.hospital.tag.manager.domain.PostTag;

@Data
public class StaffHospitalTagDTO {

    private Long postTagId;
    private String tagName;

    public StaffHospitalTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
