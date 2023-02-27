package site.hospital.tag.admin.api;


import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.tag.admin.service.AdminPostTagService;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagRequest;
import site.hospital.tag.manager.api.dto.posttag.PostTagLinkTagResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminPostTagController {

    private final AdminPostTagService adminPostTagService;


    @PostMapping("/admin/hospital/tag/link")
    public PostTagLinkTagResponse adminLinkTag(@RequestBody @Validated PostTagLinkTagRequest request) {
        return adminPostTagService.adminLinkTag(request);
    }

    //병원 태그 삭제
    @DeleteMapping("/admin/hospital/tag/delete/{postTagId}")
    public void adminDeletePostTag(@PathVariable("postTagId") Long postTagId) {
        adminPostTagService.adminDeletePostTag(postTagId);
    }
}
