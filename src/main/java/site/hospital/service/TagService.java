package site.hospital.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Tag;
import site.hospital.repository.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Long tagCreate(Tag tag) {
        validateDuplicateTag(tag);
        tagRepository.save(tag);
        return tag.getId();
    }

    public Page<Tag> searchTagName(String tagName, Pageable pageable) {
        return tagRepository.findOneByName(tagName, pageable);
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
        return tagRepository.findAll(pageable);
    }
}
