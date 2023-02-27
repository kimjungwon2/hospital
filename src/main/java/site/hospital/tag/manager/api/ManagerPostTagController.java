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
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;
import site.hospital.tag.manager.api.dto.posttag.PostTagStaffLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagViewHospitalTagResponse;
import site.hospital.tag.manager.service.ManagerPostTagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerPostTagController {

    private final ManagerPostTagService managerPostTagService;

    @PostMapping("/staff/hospital/tag/link")
    public PostTagLinkTagResponse managerLinkTag(
            ServletRequest servletRequest,
            @RequestBody @Validated PostTagStaffLinkTagRequest request
    ) {
        return managerPostTagService.managerLinkTag(servletRequest, request);
    }

    //수정 예정
    @DeleteMapping("/staff/{memberId}/hospital/tag/delete/{postTagId}")
    public void managerDeletePostTag(
            ServletRequest servletRequest,
            @PathVariable("memberId") Long memberId,
            @PathVariable("postTagId") Long postTagId
    ) {
        managerPostTagService.managerDeletePostTag(servletRequest, postTagId);
    }

    //병원 연결 태그 보기.
    @GetMapping("/hospital/tag/view/{hospitalId}")
    public List<PostTagViewHospitalTagResponse> managerViewHospitalTag(
            @PathVariable("hospitalId") Long hospitalId
    ) {
        return managerPostTagService.viewHospitalTag(hospitalId);
    }
}
