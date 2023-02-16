package site.hospital.tag.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.tag.api.dto.tag.TagAdminViewAllResponse;
import site.hospital.tag.api.dto.tag.TagCreateRequest;
import site.hospital.tag.api.dto.tag.TagCreateResponse;
import site.hospital.tag.api.dto.tag.TagSearchTagnameResponse;
import site.hospital.tag.domain.Tag;
import site.hospital.tag.repository.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

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

    //태그 삭제
    @Transactional
    public void tagDelete(Long id) {
        tagRepository.deleteById(id);
    }


    //모든 태그 보기
    public Page<Tag> allSearchTag(Pageable pageable) {
        Page<Tag> allTags = tagRepository.findAll(pageable);
        List<TagAdminViewAllResponse> collect = allTags.stream()
                .map(t -> TagAdminViewAllResponse.from(t))
                .collect(Collectors.toList());

        Long total = allTags.getTotalElements();

        return new PageImpl(collect, pageable, total);
    }
}
