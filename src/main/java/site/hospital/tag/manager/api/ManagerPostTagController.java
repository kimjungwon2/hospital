package site.hospital.tag.manager.api;

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
import site.hospital.tag.manager.api.dto.postTag.PostTagLinkTagRequest;
import site.hospital.tag.manager.api.dto.postTag.PostTagLinkTagResponse;
import site.hospital.tag.manager.api.dto.postTag.PostTagStaffLinkTagRequest;
import site.hospital.tag.manager.api.dto.postTag.PostTagViewHospitalTagResponse;
import site.hospital.tag.manager.service.ManagerPostTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerPostTagController {

    private final ManagerPostTagService managerPostTagService;

    //관계자 병원 태그 연결
    @PostMapping("/staff/hospital/tag/link")
    public PostTagLinkTagResponse staffLinkTag(ServletRequest servletRequest,
            @RequestBody @Validated PostTagStaffLinkTagRequest request) {
        return managerPostTagService.staffTagLink(servletRequest, request);
    }

    //관계자 병원 연결 태그 삭제
    @DeleteMapping("/staff/{memberId}/hospital/tag/delete/{postTagId}")
    public void staffPostTagDelete(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("postTagId") Long postTagId
    ) {
        managerPostTagService.staffPostTagDelete(servletRequest, memberId, postTagId);
    }

    //병원 연결 태그 보기.
    @GetMapping("/hospital/tag/view/{hospitalId}")
    public List<PostTagViewHospitalTagResponse> hospitalTagView(
            @PathVariable("hospitalId") Long hospitalId) {
        return managerPostTagService.viewHospitalTag(hospitalId);
    }
}
