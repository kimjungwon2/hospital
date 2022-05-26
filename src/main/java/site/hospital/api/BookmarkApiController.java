package site.hospital.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.hospital.domain.Bookmark;
import site.hospital.domain.Question;
import site.hospital.domain.hospital.BusinessCondition;
import site.hospital.dto.StaffBookmarkSearchCondition;
import site.hospital.service.BookmarkService;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotNull;
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

    //병원 관계자 즐겨찾기한 유저 검색
    @GetMapping("/staff/bookmark/search/user")
    public Page staffSearchBookmarkUsers(ServletRequest servletRequest,
                                     @RequestParam(value="nickName",required = false) String nickName,
                                     @RequestParam(value="memberIdName",required = false) String memberIdName,
                                     @RequestParam(value="phoneNumber",required = false) String phoneNumber,
                                     Pageable pageable){
        StaffBookmarkSearchCondition condition = StaffBookmarkSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).phoneNumber(phoneNumber).build();

        Page<Bookmark> bookmarks = bookmarkService.staffSearchBookmarkUsers(servletRequest, condition, pageable);

        List<SearchBookmarkUsersResponse> result = bookmarks.stream()
                .map(b->new SearchBookmarkUsersResponse(b))
                .collect(Collectors.toList());

        Long total = bookmarks.getTotalElements();

        return new PageImpl<>(result, pageable, total);
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


    /* DTO */

    @Data
    private static class CreateBookmarkRequest{
        @NotNull(message="멤버 번호가 필요합니다.")
        private Long memberId;
        @NotNull(message="병원 번호가 필요합니다.")
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
        private BusinessCondition businessCondition;
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
        private BusinessCondition businessCondition;
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
    private static class SearchBookmarkUsersResponse{
        private Long bookmarkId;
        private String memberIdName;
        private String nickName;
        private String phoneNumber;

        public SearchBookmarkUsersResponse(Bookmark bookmark) {
            this.bookmarkId = bookmark.getId();
            this.memberIdName = bookmark.getMember().getMemberIdName();
            this.nickName = bookmark.getMember().getNickName();
            this.phoneNumber = bookmark.getMember().getPhoneNumber();
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
