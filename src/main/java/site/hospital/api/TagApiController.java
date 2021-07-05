package site.hospital.api;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.CreateTagResponse;
import site.hospital.api.dto.CreateTagRequest;
import site.hospital.api.dto.DeleteTagRequest;
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

        Long id = tagService.TagCreate(tag);

        return new CreateTagResponse(id);
    }

    @DeleteMapping("/tag/delete")
    public void deleteTag(@RequestBody @Validated DeleteTagRequest request){
        tagService.TagDelete(request);
    }

}
