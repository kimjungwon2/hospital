package site.hospital.bookmark.user.api;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.bookmark.user.api.dto.BookmarkCheckResponse;
import site.hospital.bookmark.user.api.dto.BookmarkCreateRequest;
import site.hospital.bookmark.user.api.dto.BookmarkSearchMemberResponse;
import site.hospital.bookmark.user.service.BookmarkService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/user/hospital/bookmark/register")
    public void userRegisterBookmark(@RequestBody @Validated BookmarkCreateRequest request) {
        bookmarkService.userRegisterBookmark(request);
    }

    @GetMapping(value = {"/user/{memberId}/bookmark/hospital/{hospitalId}"})
    public BookmarkCheckResponse userCheckBookmark(
            @PathVariable("memberId") Long memberId,
            @PathVariable("hospitalId") Long hospitalId
    ) {
        return bookmarkService.userCheckBookmark(memberId, hospitalId);
    }

    @GetMapping("/user/{memberId}/bookmarks")
    public List<BookmarkSearchMemberResponse> userSearchBookmark(
            @PathVariable("memberId") Long memberId
    ) {
        return bookmarkService.searchUserBookmarks(memberId);
    }
}
