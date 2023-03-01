package site.hospital.tag.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.tag.admin.api.dto.TagAdminViewAllResponse;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.tag.manager.repository.TagRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminTagServiceImpl implements AdminTagService {

    private final TagRepository tagRepository;

    @Transactional
    @Override
    public void adminDeleteTag(Long id) {
        tagRepository.deleteById(id);
    }


    @Override
    public Page<Tag> adminSearchTags(Pageable pageable) {
        Page<Tag> allTags = tagRepository.findAll(pageable);
        List<TagAdminViewAllResponse> searchTags =
                allTags
                        .stream()
                        .map(TagAdminViewAllResponse::from)
                        .collect(Collectors.toList());

        Long totalAmount = allTags.getTotalElements();

        return new PageImpl(searchTags, pageable, totalAmount);
    }
}
