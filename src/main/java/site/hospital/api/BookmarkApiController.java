package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Bookmark;
import site.hospital.service.BookmarkService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class BookmarkApiController {

    private final BookmarkService bookmarkService;

    //북마크 등록 + 삭제
    @PostMapping("/user/hospital/bookmark/register")
    public void saveBookmark(@RequestBody @Validated CreateBookmarkRequest request){
        bookmarkService.bookmark(request.getMemberId(), request.getHospitalId());
    }

    //북마크 여부 확인.
    @GetMapping(value ={"/user/{memberId}/bookmark/hospital/{hospitalId}"})
    public IsBookmark isNullBookmark(@PathVariable("memberId") Long memberId,
                                     @PathVariable("hospitalId") Long hospitalId){
        Boolean isBookmark = false;

        Bookmark bookmark = bookmarkService.isBookmark(memberId, hospitalId);

        //북마크가 있으면 true 반환.
        if(bookmark != null) isBookmark = true;

        return new IsBookmark(isBookmark);
    }

    //즐겨찾기 목록 전체 조회(관리자)
    @GetMapping("/admin/bookmark/search")
    public List<SearchBookmarkResponse> searchBookmark(){
        List<Bookmark> bookmarks = bookmarkService.searchAdminBookmark();
        List<SearchBookmarkResponse> result = bookmarks.stream()
                .map(b->new SearchBookmarkResponse(b))
                .collect(Collectors.toList());

        return result;
    }

    //즐겨찾기 조회(사용자)
    @GetMapping("/user/{memberId}/bookmarks")
    public List<SearchMemberBookmarkResponse> searchMemberBookmark(@PathVariable("memberId") Long memberId){
        List<Bookmark> bookmarks = bookmarkService.searchMemberBookmark(memberId);
        List<SearchMemberBookmarkResponse> result = bookmarks.stream()
                .map(b->new SearchMemberBookmarkResponse(b))
                .collect(Collectors.toList());

        return result;
    }

    //즐겨찾기 조회(병원 관계자)
    @GetMapping("/staff/bookmark/search/{hospitalId}")
    public List<SearchHospitalBookmarkResponse> searchHospitalBookmark(@PathVariable("hospitalId") Long hospitalId){
        List<Bookmark> bookmarks = bookmarkService.searchHospitalBookmark(hospitalId);
        List<SearchHospitalBookmarkResponse> result = bookmarks.stream()
                .map(b->new SearchHospitalBookmarkResponse(b))
                .collect(Collectors.toList());

        return result;
    }

    /* DTO */

    @Data
    private static class CreateBookmarkRequest{
        private Long memberId;
        private Long hospitalId;
    }

    @Data
    private static class IsBookmark{
        private Boolean isBookmark;

        public IsBookmark(Boolean isBookmark) {
            this.isBookmark = isBookmark;
        }
    }

    @Data
    private static class SearchBookmarkResponse{
        private Long bookmarkId;
        private Long hospitalId;
        private Long memberId;
        private String medicalSubjectInformation;
        private String businessCondition;
        private String cityName;
        private String userName;
        private String hospitalName;
        private LocalDateTime createTime;

        public SearchBookmarkResponse(Bookmark bookmark) {
            this.bookmarkId = bookmark.getId();
            this.hospitalId = bookmark.getHospital().getId();
            this.memberId = bookmark.getMember().getId();
            this.medicalSubjectInformation = bookmark.getHospital().getMedicalSubjectInformation();
            this.businessCondition = bookmark.getHospital().getBusinessCondition();
            this.cityName = bookmark.getHospital().getCityName();
            this.userName = bookmark.getMember().getUserName();
            this.hospitalName = bookmark.getHospital().getHospitalName();
            this.createTime = bookmark.getCreatedDate();
        }
    }

    @Data
    private static class SearchMemberBookmarkResponse{
        private Long hospitalId;
        private String medicalSubjectInformation;
        private String businessCondition;
        private String cityName;
        private String hospitalName;
        private LocalDateTime createTime;

        public SearchMemberBookmarkResponse(Bookmark bookmark) {
            this.hospitalId = bookmark.getHospital().getId();
            this.medicalSubjectInformation = bookmark.getHospital().getMedicalSubjectInformation();
            this.businessCondition = bookmark.getHospital().getBusinessCondition();
            this.cityName = bookmark.getHospital().getCityName();
            this.hospitalName = bookmark.getHospital().getHospitalName();
            this.createTime = bookmark.getCreatedDate();
        }
    }

    @Data
    private static class SearchHospitalBookmarkResponse{
        private Long bookmarkId;
        private Long memberId;
        private String memberName;
        private String nickName;
        private String phoneNumber;
        private LocalDateTime createTime;

        public SearchHospitalBookmarkResponse(Bookmark bookmark) {
            this.bookmarkId = bookmark.getId();
            this.memberId = bookmark.getMember().getId();
            this.memberName = bookmark.getMember().getUserName();
            this.nickName = bookmark.getMember().getNickName();
            this.phoneNumber = bookmark.getMember().getPhoneNumber();
            this.createTime = bookmark.getCreatedDate();
        }
    }
}
