package site.hospital.api;

import java.util.List;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.postTag.PostTagLinkTagRequest;
import site.hospital.api.dto.postTag.PostTagLinkTagResponse;
import site.hospital.api.dto.postTag.PostTagStaffLinkTagRequest;
import site.hospital.api.dto.postTag.PostTagViewHospitalTagResponse;
import site.hospital.service.PostTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostTagApiController {

    private final PostTagService postTagService;

    //관계자 병원 태그 연결
    @PostMapping("/staff/hospital/tag/link")
    public PostTagLinkTagResponse staffLinkTag(ServletRequest servletRequest,
            @RequestBody @Validated PostTagStaffLinkTagRequest request) {
        return postTagService.staffTagLink(servletRequest, request);
    }

    //관계자 병원 연결 태그 삭제
    @DeleteMapping("/staff/{memberId}/hospital/tag/delete/{postTagId}")
    public void staffPostTagDelete(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("postTagId") Long postTagId
    ) {
        postTagService.staffPostTagDelete(servletRequest, memberId, postTagId);
    }

    //병원 태그 연결
    @PostMapping("/admin/hospital/tag/link")
    public PostTagLinkTagResponse linkTag(@RequestBody @Validated PostTagLinkTagRequest request) {
        return postTagService.tagLink(request);
    }

    //병원 태그 삭제
    @DeleteMapping("/admin/hospital/tag/delete/{postTagId}")
    public void postTagDelete(@PathVariable("postTagId") Long postTagId) {
        postTagService.postTagDelete(postTagId);
    }

    //병원 연결 태그 보기.
    @GetMapping("/hospital/tag/view/{hospitalId}")
    public List<PostTagViewHospitalTagResponse> hospitalTagView(
            @PathVariable("hospitalId") Long hospitalId) {
        return postTagService.viewHospitalTag(hospitalId);
    }
}
