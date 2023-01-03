package site.hospital.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.domain.Tag;
import site.hospital.service.PostTagService;
import site.hospital.service.TagService;

@RestController
@RequiredArgsConstructor
public class TagApiController {

    private final TagService tagService;
    private final PostTagService postTagService;

    //관계자 태그 생성
    @PostMapping("/staff/tag/create")
    public CreateTagResponse staffSaveTag(@RequestBody @Validated CreateTagRequest request) {
        Tag tag = Tag.builder().
                name(request.getTagName()).build();

        Long id = tagService.tagCreate(tag);

        return new CreateTagResponse(id);
    }

    //관계자 태그 검색
    @GetMapping("/staff/tag/search/{tagName}")
    public Page staffSearchTagName(@PathVariable("tagName") String tagName, Pageable pageable) {
        Page<Tag> findTags = tagService.searchTagName(tagName, pageable);
        List<ResponseSearchTagName> responseSearchTagName = findTags.stream()
                .map(t -> new ResponseSearchTagName(t))
                .collect(Collectors.toList());
        Long total = findTags.getTotalElements();

        return new PageImpl<>(responseSearchTagName, pageable, total);
    }

    //관리자 태그 생성
    @PostMapping("/admin/tag/create")
    public CreateTagResponse saveTag(@RequestBody @Validated CreateTagRequest request) {
        Tag tag = Tag.builder().
                name(request.getTagName()).build();
        Long id = tagService.tagCreate(tag);

        return new CreateTagResponse(id);
    }

    //관리자 태그 삭제
    @DeleteMapping("/admin/tag/delete/{tagId}")
    public void deleteTag(@PathVariable("tagId") Long tagId) {
        tagService.tagDelete(tagId);
    }

    //관리자 태그 보기
    @GetMapping("/admin/tags")
    public Page allSearchTag(Pageable pageable) {
        Page<Tag> allTags = tagService.allSearchTag(pageable);
        List<allTag> collect = allTags.stream()
                .map(t -> new allTag(t))
                .collect(Collectors.toList());

        Long total = allTags.getTotalElements();

        return new PageImpl<>(collect, pageable, total);
    }

    //관리자 태그 검색
    @GetMapping("/admin/tag/search/{tagName}")
    public Page searchTagName(@PathVariable("tagName") String tagName, Pageable pageable) {
        Page<Tag> findTags = tagService.searchTagName(tagName, pageable);
        List<ResponseSearchTagName> responseSearchTagName = findTags.stream()
                .map(t -> new ResponseSearchTagName(t))
                .collect(Collectors.toList());
        Long total = findTags.getTotalElements();

        return new PageImpl<>(responseSearchTagName, pageable, total);
    }

    /* DTO */
    @Data
    private static class CreateTagRequest {

        @NotNull(message = "태그 이름을 입력해주세요.")
        private String tagName;
    }

    @Data
    private static class CreateTagResponse {

        private Long tagId;

        public CreateTagResponse(long tagId) {
            this.tagId = tagId;
        }
    }

    @Data
    private static class ResponseSearchTagName {

        private Long tagId;
        private String name;
        private LocalDateTime createdDate;

        public ResponseSearchTagName(Tag tag) {
            this.tagId = tag.getId();
            this.name = tag.getName();
            this.createdDate = tag.getCreatedDate();
        }
    }

    @Data
    private static class allTag {

        private Long tagId;
        private String name;
        private LocalDateTime createdDate;

        public allTag(Tag tag) {
            this.tagId = tag.getId();
            this.name = tag.getName();
            this.createdDate = tag.getCreatedDate();
        }
    }

}
