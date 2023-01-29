package site.hospital.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.hospital.api.dto.bookmark.BookmarkAdminSearchMemberResponse;
import site.hospital.api.dto.bookmark.BookmarkCheckResponse;
import site.hospital.api.dto.bookmark.BookmarkCreateRequest;
import site.hospital.api.dto.bookmark.BookmarkSearchMemberResponse;
import site.hospital.api.dto.bookmark.BookmarkSearchResponse;
import site.hospital.domain.Bookmark;
import site.hospital.dto.StaffBookmarkSearchCondition;
import site.hospital.service.BookmarkService;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    //북마크 등록 + 삭제
    @PostMapping("/user/hospital/bookmark/register")
    public void saveBookmark(@RequestBody @Validated BookmarkCreateRequest request) {
        bookmarkService.bookmark(request.getMemberId(), request.getHospitalId());
    }

    //북마크 여부 확인.
    @GetMapping(value = {"/user/{memberId}/bookmark/hospital/{hospitalId}"})
    public BookmarkCheckResponse isNullBookmark(@PathVariable("memberId") Long memberId,
            @PathVariable("hospitalId") Long hospitalId) {
        Boolean isBookmark = false;

        Bookmark bookmark = bookmarkService.isBookmark(memberId, hospitalId);

        //북마크가 있으면 true 반환.
        if (bookmark != null) {
            isBookmark = true;
        }

        return BookmarkCheckResponse.from(isBookmark);
    }

    //병원 관계자 즐겨찾기한 유저 검색
    @GetMapping("/staff/bookmark/search/user")
    public Page staffSearchBookmarkUsers(ServletRequest servletRequest,
            @RequestParam(value = "nickName", required = false) String nickName,
            @RequestParam(value = "memberIdName", required = false) String memberIdName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            Pageable pageable) {
        StaffBookmarkSearchCondition condition = StaffBookmarkSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).phoneNumber(phoneNumber).build();

        Page<Bookmark> bookmarks = bookmarkService
                .staffSearchBookmarkUsers(servletRequest, condition, pageable);

        List<BookmarkAdminSearchMemberResponse> result = bookmarks.stream()
                .map(b -> BookmarkAdminSearchMemberResponse.from(b))
                .collect(Collectors.toList());

        Long total = bookmarks.getTotalElements();

        return new PageImpl<>(result, pageable, total);
    }

    //즐겨찾기 목록 전체 조회(관리자)
    @GetMapping("/admin/bookmark/search")
    public List<BookmarkSearchResponse> searchBookmark() {
        List<Bookmark> bookmarks = bookmarkService.searchAdminBookmark();
        List<BookmarkSearchResponse> result = bookmarks.stream()
                .map(b -> BookmarkSearchResponse.from(b))
                .collect(Collectors.toList());

        return result;
    }

    //즐겨찾기 조회(사용자)
    @GetMapping("/user/{memberId}/bookmarks")
    public List<BookmarkSearchMemberResponse> searchMemberBookmark(
            @PathVariable("memberId") Long memberId) {
        List<Bookmark> bookmarks = bookmarkService.searchMemberBookmark(memberId);
        List<BookmarkSearchMemberResponse> result = bookmarks.stream()
                .map(b -> BookmarkSearchMemberResponse.from(b))
                .collect(Collectors.toList());

        return result;
    }


}
