package site.hospital.bookmark.admin.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.bookmark.admin.service.AdminBookmarkService;
import site.hospital.bookmark.user.api.dto.BookmarkSearchResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminBookmarkController {

    private final AdminBookmarkService adminBookmarkService;

    //즐겨찾기 목록 전체 조회(관리자)
    @GetMapping("/admin/bookmark/search")
    public List<BookmarkSearchResponse> searchBookmark() {
        return adminBookmarkService.searchAdminBookmark();
    }

}
