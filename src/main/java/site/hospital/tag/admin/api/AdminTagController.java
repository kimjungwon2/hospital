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

    //관리자 태그 생성
    @PostMapping("/admin/tag/create")
    public TagCreateResponse saveTag(@RequestBody @Validated TagCreateRequest request) {
        return managerTagService.tagCreate(request);
    }

    //관리자 태그 삭제
    @DeleteMapping("/admin/tag/delete/{tagId}")
    public void deleteTag(@PathVariable("tagId") Long tagId) {
        adminTagService.tagDelete(tagId);
    }

    //관리자 태그 보기
    @GetMapping("/admin/tags")
    public Page allSearchTag(Pageable pageable) {
        return adminTagService.allSearchTag(pageable);
    }

    //관리자 태그 검색
    @GetMapping("/admin/tag/search/{tagName}")
    public Page searchTagName(
            @PathVariable("tagName") String tagName,
            Pageable pageable
    ) {
        return managerTagService.searchTagName(tagName, pageable);
    }

}
