package site.hospital.tag.manager.service;

import java.util.List;
import javax.servlet.ServletRequest;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;
import site.hospital.tag.manager.api.dto.posttag.PostTagStaffLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagViewHospitalTagResponse;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.tag.manager.domain.Tag;


public interface ManagerPostTagService {

    PostTagLinkTagResponse managerLinkTag(
            ServletRequest servletRequest,
            PostTagStaffLinkTagRequest request
    );

    PostTag linkTag(Tag tag, Hospital hospital);

    void managerDeletePostTag(
            ServletRequest servletRequest,
            Long postTagId
    );

    List<PostTagViewHospitalTagResponse> viewHospitalTag(Long hospitalId);

    void validateDuplicateLinkTag(Tag tag, Hospital hospital);

}
