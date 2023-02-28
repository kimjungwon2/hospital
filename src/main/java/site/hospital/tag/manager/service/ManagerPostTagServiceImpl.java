package site.hospital.tag.manager.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.common.service.ManagerJwtService;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;
import site.hospital.tag.manager.api.dto.posttag.PostTagStaffLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagViewHospitalTagResponse;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.tag.manager.repository.PostTagRepository;
import site.hospital.tag.manager.repository.TagRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerPostTagServiceImpl implements ManagerPostTagService {

    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final HospitalRepository hospitalRepository;
    private final ManagerJwtService managerJwtService;

    @Transactional
    @Override
    public PostTagLinkTagResponse managerLinkTag(
            ServletRequest servletRequest,
            PostTagStaffLinkTagRequest request
    ) {
        managerJwtService.accessManager(servletRequest, request.getMemberId(), request.getHospitalId());

        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        validateDuplicateLinkTag(tag, hospital);
        PostTag postTag = linkTag(tag, hospital);

        return PostTagLinkTagResponse.from(postTag.getId());
    }

    @Override
    public PostTag linkTag(Tag tag, Hospital hospital) {
        PostTag postTag = PostTag.createPostTag(tag, hospital);
        postTagRepository.save(postTag);
        return postTag;
    }

    @Transactional
    @Override
    public void managerDeletePostTag(
            ServletRequest servletRequest,
            Long memberId,
            Long postTagId
    ) {
        PostTag postTag = postTagRepository.findById(postTagId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 연결 태그가 존재하지 않습니다."));

        managerJwtService.accessManager(servletRequest, memberId, postTag.getHospital().getId());

        postTagRepository.deleteById(postTagId);
    }

    @Override
    public List<PostTagViewHospitalTagResponse> viewHospitalTag(Long hospitalId) {
        List<PostTag> PostTags = postTagRepository.viewPostTags(hospitalId);

        return PostTags
                .stream()
                .map(p -> PostTagViewHospitalTagResponse.from(p))
                .collect(Collectors.toList());
    }

    @Override
    public void validateDuplicateLinkTag(Tag tag, Hospital hospital) {
        PostTag findLinkTag = postTagRepository.findByTagAndHospital(tag, hospital);

        if (findLinkTag != null) {
            throw new IllegalStateException("이미 존재하는 태그 연결.");
        }
    }

}
