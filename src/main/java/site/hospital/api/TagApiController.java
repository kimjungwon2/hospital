package site.hospital.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.hospital.domain.Tag;
import site.hospital.service.TagService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TagApiController {
    private final TagService tagService;

    @PostMapping("/admin/tag/create")
    public CreateTagResponse saveTag(@RequestBody @Validated CreateTagRequest request){
        Tag tag = Tag.builder().
                name(request.getTagName()).build();
        Long id = tagService.tagCreate(tag);

        return new CreateTagResponse(id);
    }

    @DeleteMapping("/admin/tag/delete")
    public void deleteTag(@RequestBody @Validated DeleteTagRequest request){
        tagService.tagDelete(request.getTagId());
    }

    @GetMapping("/admin/tag/search")
    public Result allSearchTag(){
        List<Tag> allTags = tagService.allSearchTag();
        List<allTag> collect = allTags.stream()
                .map(t-> new allTag(t))
                .collect(Collectors.toList());

        return new Result(collect);
    }


    /* DTO */
    @Data
    private static class CreateTagRequest {
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
    private static class DeleteTagRequest {
        private Long tagId;
    }

    @Data
    @AllArgsConstructor
    private static class Result<T>{
        private T Data;
    }

    @Data
    private static class allTag{
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
