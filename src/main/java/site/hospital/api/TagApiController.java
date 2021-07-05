package site.hospital.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @PostMapping("/tag/create")
    public CreateTagResponse saveTag(@RequestBody @Validated CreateTagRequest request){
        Tag tag = Tag.builder().
                name(request.getName()).build();
        Long id = tagService.tagCreate(tag);

        return new CreateTagResponse(id);
    }

    @DeleteMapping("/tag/delete")
    public void deleteTag(@RequestBody @Validated DeleteTagRequest request){
        tagService.tagDelete(request.getTagId());
    }

    @GetMapping("/tag/search")
    public Result allSearchTag(){
        List<Tag> allTags = tagService.allSearchTag();
        List<allTag> collect = allTags.stream()
                .map(t-> new allTag(t.getId(), t.getName(), t.getCreatedDate()))
                .collect(Collectors.toList());

        return new Result(collect);
    }



    /* DTO */
    @Data
    static class CreateTagRequest {
        private String name;
    }

    @Data
    static class CreateTagResponse {
        private long tagId;
        public CreateTagResponse(long tagId) {
            this.tagId = tagId;
        }
    }

    @Data
    static class DeleteTagRequest {
        private Long tagId;
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T Data;
    }

    @Data
    static class allTag{
        private Long tagId;
        private String name;
        private LocalDateTime createdDate;

        public allTag(Long tagId, String name, LocalDateTime createdDate) {
            this.tagId = tagId;
            this.name = name;
            this.createdDate = createdDate;
        }
    }

}
