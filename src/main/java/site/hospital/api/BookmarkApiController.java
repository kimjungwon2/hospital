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

    @PostMapping("/hospital/bookmark")
    public CreateBookmarkResponse saveBookmark(@RequestBody @Validated CreateBookmarkRequest request){
        Long id = bookmarkService.bookmark(request.getMemberId(), request.hospitalId);

        return new CreateBookmarkResponse(id);
    }

    //예약 목록 전체 조회(관리자)
    @GetMapping("/search/bookmark")
    public List<SearchBookmarkResponse> searchBookmark(){
        List<Bookmark> bookmarks = bookmarkService.searchAdminBookmark();
        List<SearchBookmarkResponse> result = bookmarks.stream()
                .map(b->new SearchBookmarkResponse(b))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/member/bookmark/{memberId}")
    public List<SearchMemberBookmarkResponse> searchMemberBookmark(@PathVariable("memberId") Long memberId){
        List<Bookmark> bookmarks = bookmarkService.searchMemberBookmark(memberId);
        List<SearchMemberBookmarkResponse> result = bookmarks.stream()
                .map(b->new SearchMemberBookmarkResponse(b))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/member/bookmark/{hospitalId}")
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
        long id;
        public CreateBookmarkResponse(long id) {
            this.id = id;
        }
    }

    @Data
    private static class CreateBookmarkRequest{
        long memberId;
        long hospitalId;
    }

    @Data
    private static class SearchBookmarkResponse{
        long bookmarkId;
        long hospitalId;
        long memberId;
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
        long hospitalId;
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
        long bookmarkId;
        long memberId;
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
