package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.PostTag;
import site.hospital.service.PostTagService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostTagApiController {

    private final PostTagService postTagService;

    @PostMapping("/tag/link")
    public LinkTagResponse linkTag(@RequestBody @Validated LinkTagRequest request){
        Long id = postTagService.tagLink(request.getTagId(), request.getHospitalId());

        return new LinkTagResponse(id);
    }

    @GetMapping("/hospital/tag/view/{hospitalId}")
    public List<hospitalTagViewResponse> hospitalTagView(@PathVariable("hospitalId") Long hospitalId){
        List<PostTag> postTags = postTagService.viewHospitalTag(hospitalId);
        List<hospitalTagViewResponse> result = postTags.stream()
                .map(p->new hospitalTagViewResponse(p))
                .collect(Collectors.toList());

        return result;
    }


    /* DTO */
    @Data
    private static class LinkTagRequest {
        private Long tagId;
        private Long hospitalId;
    }

    @Data
    private static class LinkTagResponse {
        private Long postTagId;
        public LinkTagResponse(long postTagId) {
            this.postTagId = postTagId;
        }
    }

    @Data
    private static class hospitalTagViewResponse {
        private String tagName;
        private Long tagId;

        public hospitalTagViewResponse(PostTag postTag) {
            this.tagName = postTag.getTag().getName();
            this.tagId = postTag.getTag().getId();
        }
    }

}
