package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.Hospital;
import site.hospital.domain.PostTag;
import site.hospital.domain.Tag;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.TagRepository;
import site.hospital.repository.postTag.PostTagRepository;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final HospitalRepository hospitalRepository;

    @Transactional
    public Long tagLink(Long tagId, Long hospitalId){
        Tag tag = tagRepository.findById(tagId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        validateDuplicateLinkTag(tag, hospital);

        PostTag postTag = PostTag.createPostTag(tag,hospital);
        postTagRepository.save(postTag);

        return postTag.getId();
    }

    public List<PostTag> viewHospitalTag(Long hospitalId){
        List<PostTag> PostTags = postTagRepository.listPostTag(hospitalId);

        return PostTags;
    }


    //태그 연결 중복 확인.
    private void validateDuplicateLinkTag(Tag tag,Hospital hospital){
        PostTag findLinkTag= postTagRepository.findByTagAndHospital(tag, hospital);
        if(findLinkTag != null){
            throw new IllegalStateException("이미 존재하는 태그 연결.");
        }
    }

}
