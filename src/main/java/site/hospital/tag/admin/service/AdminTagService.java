package site.hospital.tag.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.tag.manager.domain.Tag;

public interface AdminTagService {

    void adminDeleteTag(Long id);

    Page<Tag> adminSearchTags(Pageable pageable);
}
