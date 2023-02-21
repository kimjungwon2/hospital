package site.hospital.tag.admin.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.tag.admin.service.AdminTagService;
import site.hospital.tag.manager.api.dto.tag.TagCreateRequest;
import site.hospital.tag.manager.api.dto.tag.TagCreateResponse;
import site.hospital.tag.manager.service.ManagerTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminTagController {

    private final ManagerTagService managerTagService;
    private final AdminTagService adminTagService;

    @PostMapping("/admin/tag/create")
    public TagCreateResponse adminCreateTag(@RequestBody @Validated TagCreateRequest request) {
        return managerTagService.createTag(request);
    }

    @DeleteMapping("/admin/tag/delete/{tagId}")
    public void adminDeleteTag(@PathVariable("tagId") Long tagId) {
        adminTagService.adminDeleteTag(tagId);
    }

    @GetMapping("/admin/tags")
    public Page adminSearchTags(Pageable pageable) {
        return adminTagService.adminSearchTags(pageable);
    }

    @GetMapping("/admin/tag/search/{tagName}")
    public Page searchTagName(
            @PathVariable("tagName") String tagName,
            Pageable pageable
    ) {
        return managerTagService.searchTagName(tagName, pageable);
    }

}
