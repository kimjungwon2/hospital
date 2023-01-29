package site.hospital.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.api.dto.bookmark.BookmarkAdminSearchMemberResponse;
import site.hospital.api.dto.bookmark.BookmarkCheckResponse;
import site.hospital.api.dto.bookmark.BookmarkCreateRequest;
import site.hospital.api.dto.bookmark.BookmarkSearchMemberResponse;
import site.hospital.api.dto.bookmark.BookmarkSearchResponse;
import site.hospital.domain.Bookmark;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.dto.StaffBookmarkSearchCondition;
import site.hospital.repository.bookmark.BookmarkRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final JwtStaffAccessService jwtStaffAccessService;

    //북마크 여부 확인.
    public BookmarkCheckResponse isBookmark(Long memberId, Long hospitalId) {
        Boolean isBookmark = false;

        Bookmark bookmark = bookmarkRepository.isUserBookmark(memberId, hospitalId);

        //북마크가 있으면 true 반환.
        if (bookmark != null) {
            isBookmark = true;
        }

        return BookmarkCheckResponse.from(isBookmark);
    }

    @Transactional
    public void bookmark(BookmarkCreateRequest request) {

        //북마크 했는지 체크 여부
        Boolean existence = false;

        //북마크 있는지 확인.
        Bookmark isBookmark = bookmarkRepository
                .isUserBookmark(request.getMemberId(), request.getHospitalId());

        //북마크 존재 시, true
        if (isBookmark != null) {
            existence = true;
        }

        //북마크가 있으면 삭제.
        if (existence == true) {
            bookmarkRepository.delete(isBookmark);
        }

        //북마크 여부가 없으면 북마크 저장
        else {
            Member member = memberRepository
                    .findById(request.getMemberId())
                    .orElseThrow(
                            () -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));

            Hospital hospital = hospitalRepository
                    .findById(request.getHospitalId())
                    .orElseThrow(
                            () -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));
            Bookmark bookmark = Bookmark.createBookmark(member, hospital);
            bookmarkRepository.save(bookmark);
        }
    }

    //병원 관계자 즐겨찾기 검색
    public Page<Bookmark> staffSearchBookmarkUsers(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            String phoneNumber,
            Pageable pageable
    ) {
        StaffBookmarkSearchCondition condition =
                StaffBookmarkSearchCondition
                        .builder()
                        .nickName(nickName)
                        .memberIdName(memberIdName)
                        .phoneNumber(phoneNumber)
                        .build();

        Long JwtHospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);

        Page<Bookmark> bookmarks = bookmarkRepository
                .staffSearchBookmark(JwtHospitalId, condition, pageable);

        List<BookmarkAdminSearchMemberResponse> content =
                bookmarks.stream()
                        .map(b -> BookmarkAdminSearchMemberResponse.from(b))
                        .collect(Collectors.toList());

        Long total = bookmarks.getTotalElements();

        return new PageImpl(content, pageable, total);
    }

    //관리자 즐겨찾기 조회
    public List<BookmarkSearchResponse> searchAdminBookmark() {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(null, null);
        List<BookmarkSearchResponse> result =
                bookmarks.stream()
                        .map(b -> BookmarkSearchResponse.from(b))
                        .collect(Collectors.toList());

        return result;
    }

    //멤버 즐겨찾기 조회
    public List<BookmarkSearchMemberResponse> searchMemberBookmark(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(memberId, null);
        List<BookmarkSearchMemberResponse> result = bookmarks.stream()
                .map(b -> BookmarkSearchMemberResponse.from(b))
                .collect(Collectors.toList());

        return result;
    }
}
