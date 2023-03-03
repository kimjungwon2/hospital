package site.hospital.tag.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.tag.manager.repository.PostTagRepository;
import site.hospital.tag.manager.repository.TagRepository;
import site.hospital.tag.manager.service.ManagerPostTagService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminPostTagServiceImpl implements AdminPostTagService {

    private final TagRepository tagRepository;
    private final HospitalRepository hospitalRepository;
    private final ManagerPostTagService managerPostTagService;
    private final PostTagRepository postTagRepository;

    @Transactional
    @Override
    public PostTagLinkTagResponse adminLinkTag(PostTagLinkTagRequest request) {
        Tag tag = tagRepository.findById(request.getTagId())
                .orElseThrow(() -> new IllegalStateException("태그가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("병원이 존재하지 않습니다."));

        managerPostTagService.validateDuplicateLinkTag(tag, hospital);
        PostTag postTag = managerPostTagService.linkTag(tag,hospital);

        return PostTagLinkTagResponse.from(postTag.getId());
    }

    @Transactional
    @Override
    public void adminDeletePostTag(Long postTagId) {
        postTagRepository.findById(postTagId)
                .orElseThrow(
                        () -> new IllegalStateException("연결 태그가 존재하지 않습니다."));
        postTagRepository.deleteById(postTagId);
    }

}
