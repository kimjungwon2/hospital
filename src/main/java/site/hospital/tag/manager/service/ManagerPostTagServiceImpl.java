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
        managerJwtService.accessManager(servletRequest, request.getHospitalId());

        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new IllegalStateException("태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("병원이 존재하지 않습니다."));

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
            Long postTagId
    ) {
        PostTag postTag = postTagRepository.findById(postTagId)
                .orElseThrow(() -> new IllegalStateException("연결 태그가 존재하지 않습니다."));

        managerJwtService.accessManager(servletRequest, postTag.getHospital().getId());

        postTagRepository.deleteById(postTagId);
    }

    @Override
    public List<PostTagViewHospitalTagResponse> viewHospitalTag(Long hospitalId) {
        List<PostTag> postTags = postTagRepository.viewPostTags(hospitalId);

        return postTags
                .stream()
                .map(PostTagViewHospitalTagResponse::from)
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
