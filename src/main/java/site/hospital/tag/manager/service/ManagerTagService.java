package site.hospital.tag.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.tag.manager.api.dto.tag.TagCreateRequest;
import site.hospital.tag.manager.api.dto.tag.TagCreateResponse;
import site.hospital.tag.manager.api.dto.tag.TagSearchTagnameResponse;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.tag.manager.repository.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerTagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagCreateResponse tagCreate(TagCreateRequest request) {
        Tag tag = Tag.builder().name(request.getTagName()).build();

        validateDuplicateTag(tag);
        tagRepository.save(tag);

        return TagCreateResponse.from(tag.getId());
    }

    public Page<Tag> searchTagName(String tagName, Pageable pageable) {
        Page<Tag> findTags = tagRepository.findOneByName(tagName, pageable);

        List<TagSearchTagnameResponse> responseSearchTagName = findTags.stream()
                .map(t -> TagSearchTagnameResponse.from(t))
                .collect(Collectors.toList());
        Long total = findTags.getTotalElements();

        return new PageImpl(responseSearchTagName, pageable, total);
    }

    private void validateDuplicateTag(Tag tag) {
        List<Tag> findTag = tagRepository.findByName(tag.getName());
        if (!findTag.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 태그이름.");
        }
    }
}