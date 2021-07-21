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

    @PostMapping("/user/bookmark/register")
    public CreateBookmarkResponse saveBookmark(@RequestBody @Validated CreateBookmarkRequest request){
        Long id = bookmarkService.bookmark(request.getMemberId(), request.hospitalId);

        return new CreateBookmarkResponse(id);
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
    @GetMapping("/user/bookmark/search/{memberId}")
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
    private static class CreateBookmarkResponse{
        Long id;
        public CreateBookmarkResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateBookmarkRequest{
        Long memberId;
        Long hospitalId;
    }

    @Data
    private static class SearchBookmarkResponse{
        Long bookmarkId;
        Long hospitalId;
        Long memberId;
        String medicalSubjectInformation;
        String businessCondition;
        String cityName;
        String userName;
        String hospitalName;
        LocalDateTime createTime;

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
        Long hospitalId;
        String medicalSubjectInformation;
        String businessCondition;
        String cityName;
        String hospitalName;
        LocalDateTime createTime;

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
        Long bookmarkId;
        Long memberId;
        String memberName;
        String nickName;
        String phoneNumber;
        LocalDateTime createTime;

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
