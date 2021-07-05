package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import site.hospital.domain.Tag;
import site.hospital.service.TagService;

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



    /* DTO */
    @Data
    public class CreateTagRequest {
        private String name;
    }

    @Data
    public class CreateTagResponse {
        private long tagId;
        public CreateTagResponse(long tagId) {
            this.tagId = tagId;
        }
    }

    @Data
    public class DeleteTagRequest {
        private Long tagId;
    }

}
