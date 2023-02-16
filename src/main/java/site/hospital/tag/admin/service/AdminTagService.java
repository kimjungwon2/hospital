package site.hospital.tag.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.tag.manager.api.dto.tag.TagAdminViewAllResponse;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.tag.manager.repository.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminTagService {

    private final TagRepository tagRepository;

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
