package site.hospital.tag.manager.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.tag.manager.api.dto.tag.TagCreateRequest;
import site.hospital.tag.manager.api.dto.tag.TagCreateResponse;
import site.hospital.tag.manager.domain.Tag;

public interface ManagerTagService {

    TagCreateResponse createTag(TagCreateRequest request);

    Page<Tag> searchTagName(String tagName, Pageable pageable);

}
