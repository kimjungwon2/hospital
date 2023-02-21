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

    @GetMapping("/admin/bookmark/search")
    public List<BookmarkSearchResponse> adminSearchBookmarkUsers() {
        return adminBookmarkService.adminSearchBookmarkUsers();
    }

}
