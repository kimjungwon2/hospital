package site.hospital.tag.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtAccessService;
import site.hospital.tag.manager.api.dto.postTag.PostTagLinkTagRequest;
import site.hospital.tag.manager.api.dto.postTag.PostTagLinkTagResponse;
import site.hospital.tag.manager.api.dto.postTag.PostTagStaffLinkTagRequest;
import site.hospital.tag.manager.api.dto.postTag.PostTagViewHospitalTagResponse;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.tag.manager.repository.TagRepository;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.tag.manager.repository.PostTagRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerPostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final HospitalRepository hospitalRepository;
    private final ManagerJwtAccessService managerJwtAccessService;

    //병원 관계자 태그 연결
    @Transactional
    public PostTagLinkTagResponse staffTagLink(ServletRequest servletRequest, PostTagStaffLinkTagRequest request) {
        managerJwtAccessService.staffAccessFunction(servletRequest, request.getMemberId(), request.getHospitalId());

        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        validateDuplicateLinkTag(tag, hospital);

        PostTag postTag = PostTag.createPostTag(tag, hospital);
        postTagRepository.save(postTag);

        return PostTagLinkTagResponse.from(postTag.getId());
    }

    //병원 관계자 등록 태그 삭제
    @Transactional
    public void staffPostTagDelete(ServletRequest servletRequest, Long memberId, Long postTagId) {
        PostTag postTag = postTagRepository.findById(postTagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 연결 태그가 존재하지 않습니다."));

        managerJwtAccessService
                .staffAccessFunction(servletRequest, memberId, postTag.getHospital().getId());

        postTagRepository.deleteById(postTagId);
    }

    public List<PostTagViewHospitalTagResponse> viewHospitalTag(Long hospitalId) {
        List<PostTag> PostTags = postTagRepository.listPostTag(hospitalId);
        List<PostTagViewHospitalTagResponse> result = PostTags.stream()
                .map(p -> PostTagViewHospitalTagResponse.from(p))
                .collect(Collectors.toList());

        return result;
    }

    //태그 연결 중복 확인.
    public void validateDuplicateLinkTag(Tag tag, Hospital hospital) {
        PostTag findLinkTag = postTagRepository.findByTagAndHospital(tag, hospital);

        if (findLinkTag != null) {
            throw new IllegalStateException("이미 존재하는 태그 연결.");
        }
    }

}
