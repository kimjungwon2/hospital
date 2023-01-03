package site.hospital.service;

import java.util.List;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.PostTag;
import site.hospital.domain.Tag;
import site.hospital.domain.hospital.Hospital;
import site.hospital.repository.TagRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.postTag.PostTagRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final HospitalRepository hospitalRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

    //병원 관계자 태그 연결
    @Transactional
    public Long staffTagLink(ServletRequest servletRequest, Long tagId, Long memberId,
            Long hospitalId) {
        jwtStaffAccessService.staffAccessFunction(servletRequest, memberId, hospitalId);

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        validateDuplicateLinkTag(tag, hospital);

        PostTag postTag = PostTag.createPostTag(tag, hospital);
        postTagRepository.save(postTag);

        return postTag.getId();
    }

    //병원 관계자 등록 태그 삭제
    @Transactional
    public void staffPostTagDelete(ServletRequest servletRequest, Long memberId, Long postTagId) {
        PostTag postTag = postTagRepository.findById(postTagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 연결 태그가 존재하지 않습니다."));

        jwtStaffAccessService
                .staffAccessFunction(servletRequest, memberId, postTag.getHospital().getId());

        postTagRepository.deleteById(postTagId);
    }

    @Transactional
    public Long tagLink(Long tagId, Long hospitalId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        validateDuplicateLinkTag(tag, hospital);

        PostTag postTag = PostTag.createPostTag(tag, hospital);
        postTagRepository.save(postTag);

        return postTag.getId();
    }

    public List<PostTag> viewHospitalTag(Long hospitalId) {
        List<PostTag> PostTags = postTagRepository.listPostTag(hospitalId);

        return PostTags;
    }

    //관리자 병원 등록 태그 삭제
    @Transactional
    public void postTagDelete(Long postTagId) {
        PostTag postTag = postTagRepository.findById(postTagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 연결 태그가 존재하지 않습니다."));
        postTagRepository.deleteById(postTagId);
    }


    //태그 연결 중복 확인.
    private void validateDuplicateLinkTag(Tag tag, Hospital hospital) {
        PostTag findLinkTag = postTagRepository.findByTagAndHospital(tag, hospital);

        if (findLinkTag != null) {
            throw new IllegalStateException("이미 존재하는 태그 연결.");
        }
    }

}
