package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.tag.TagCreateRequest;
import site.hospital.api.dto.tag.TagCreateResponse;
import site.hospital.service.TagService;

@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService tagService;

    //관계자 태그 생성
    @PostMapping("/staff/tag/create")
    public TagCreateResponse staffSaveTag(@RequestBody @Validated TagCreateRequest request) {
        return tagService.tagCreate(request);
    }

    //관계자 태그 검색
    @GetMapping("/staff/tag/search/{tagName}")
    public Page staffSearchTagName(
            @PathVariable("tagName") String tagName,
            Pageable pageable
    ) {
        return tagService.searchTagName(tagName, pageable);
    }

    //관리자 태그 생성
    @PostMapping("/admin/tag/create")
    public TagCreateResponse saveTag(@RequestBody @Validated TagCreateRequest request) {
        return tagService.tagCreate(request);
    }

    //관리자 태그 삭제
    @DeleteMapping("/admin/tag/delete/{tagId}")
    public void deleteTag(@PathVariable("tagId") Long tagId) {
        tagService.tagDelete(tagId);
    }

    //관리자 태그 보기
    @GetMapping("/admin/tags")
    public Page allSearchTag(Pageable pageable) {
        return tagService.allSearchTag(pageable);
    }

    //관리자 태그 검색
    @GetMapping("/admin/tag/search/{tagName}")
    public Page searchTagName(
            @PathVariable("tagName") String tagName,
            Pageable pageable
    ) {
        return tagService.searchTagName(tagName, pageable);
    }

}
