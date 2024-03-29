package site.hospital.tag.manager.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.tag.manager.api.dto.tag.TagCreateRequest;
import site.hospital.tag.manager.api.dto.tag.TagCreateResponse;
import site.hospital.tag.manager.service.ManagerTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerTagController {

    private final ManagerTagService managerTagService;

    @PostMapping("/staff/tag/create")
    public TagCreateResponse managerCreateTag(@RequestBody @Validated TagCreateRequest request) {
        return managerTagService.createTag(request);
    }

    @GetMapping("/staff/tag/search/{tagName}")
    public Page managerSearchTagName(
            @PathVariable("tagName") String tagName,
            Pageable pageable
    ) {
        return managerTagService.searchTagName(tagName, pageable);
    }

}
