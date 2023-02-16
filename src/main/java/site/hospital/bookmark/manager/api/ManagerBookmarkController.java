package site.hospital.bookmark.manager.api;

import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.bookmark.manager.service.ManagerBookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ManagerBookmarkController {

    private final ManagerBookmarkService managerBookmarkService;

    //병원 관계자 즐겨찾기한 유저 검색
    @GetMapping("/staff/bookmark/search/user")
    public Page staffSearchBookmarkUsers(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            Pageable pageable
    ) {

        return managerBookmarkService
                .staffSearchBookmarkUsers(
                        servletRequest,
                        nickName,
                        memberIdName,
                        phoneNumber,
                        pageable
                );
    }
}
