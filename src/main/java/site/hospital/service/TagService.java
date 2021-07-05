package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import site.hospital.domain.Tag;
import site.hospital.repository.TagRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public Long tagCreate(Tag tag){
        validateDuplicateTag(tag);
        tagRepository.save(tag);
        return tag.getId();
    }

    private void validateDuplicateTag(Tag tag){
        List<Tag> findTag= tagRepository.findByName(tag.getName());
        if(!findTag.isEmpty()){
            throw new IllegalStateException("이미 존재하는 태그이름.");
        }
    }

    @Transactional
    public void tagDelete(Long id){
        tagRepository.deleteById(id);
    }
}
