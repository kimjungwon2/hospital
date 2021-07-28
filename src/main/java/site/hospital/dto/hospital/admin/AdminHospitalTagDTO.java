package site.hospital.dto.hospital.admin;

import site.hospital.domain.PostTag;

public class AdminHospitalTagDTO {
    private Long postTagId;
    private String tagName;

    public AdminHospitalTagDTO(PostTag postTag) {
        this.postTagId = postTag.getId();
        this.tagName = postTag.getTag().getName();
    }
}
