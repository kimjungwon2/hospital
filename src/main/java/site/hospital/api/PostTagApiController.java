package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.PostTag;
import site.hospital.service.PostTagService;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostTagApiController {

    private final PostTagService postTagService;

    //관계자 병원 태그 연결
    @PostMapping("/staff/hospital/tag/link")
    public LinkTagResponse staffLinkTag(ServletRequest servletRequest, @RequestBody @Validated StaffLinkTagRequest request){
        Long id = postTagService.staffTagLink(servletRequest, request.getTagId(), request.getMemberId(), request.getHospitalId());

        return new LinkTagResponse(id);
    }

    //관계자 병원 연결 태그 삭제
    @DeleteMapping("/staff/{memberId}/hospital/tag/delete/{postTagId}")
    public void staffPostTagDelete(ServletRequest servletRequest,  @PathVariable("memberId") Long memberId, @PathVariable("postTagId") Long postTagId){
        postTagService.staffPostTagDelete(servletRequest, memberId, postTagId);
    }

    //병원 태그 연결
    @PostMapping("/admin/hospital/tag/link")
    public LinkTagResponse linkTag(@RequestBody @Validated LinkTagRequest request){
        Long id = postTagService.tagLink(request.getTagId(), request.getHospitalId());

        return new LinkTagResponse(id);
    }

    //병원 태그 삭제
    @DeleteMapping("/admin/hospital/tag/delete/{postTagId}")
    public void postTagDelete(@PathVariable("postTagId") Long postTagId){
        postTagService.postTagDelete(postTagId);
    }

    //병원 연결 태그 보기.
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
    private static class StaffLinkTagRequest {
        private Long memberId;
        private Long tagId;
        private Long hospitalId;
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
