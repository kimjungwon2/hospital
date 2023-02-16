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

    //북마크 등록 + 삭제
    @PostMapping("/user/hospital/bookmark/register")
    public void saveBookmark(@RequestBody @Validated BookmarkCreateRequest request) {
        bookmarkService.bookmark(request);
    }

    //북마크 여부 확인.
    @GetMapping(value = {"/user/{memberId}/bookmark/hospital/{hospitalId}"})
    public BookmarkCheckResponse isNullBookmark(
            @PathVariable("memberId") Long memberId,
            @PathVariable("hospitalId") Long hospitalId
    ) {
        return bookmarkService.isBookmark(memberId, hospitalId);
    }

    //즐겨찾기 조회(사용자)
    @GetMapping("/user/{memberId}/bookmarks")
    public List<BookmarkSearchMemberResponse> searchMemberBookmark(
            @PathVariable("memberId") Long memberId
    ) {
        return bookmarkService.searchMemberBookmark(memberId);
    }

}
