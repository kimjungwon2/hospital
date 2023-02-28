package site.hospital.tag.admin.service;

import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;

public interface AdminPostTagService {

    PostTagLinkTagResponse adminLinkTag(PostTagLinkTagRequest request);

    void adminDeletePostTag(Long postTagId);

}
