package site.hospital.bookmark.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.bookmark.user.api.dto.BookmarkCheckResponse;
import site.hospital.bookmark.user.api.dto.BookmarkCreateRequest;
import site.hospital.bookmark.user.api.dto.BookmarkSearchMemberResponse;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.BookmarkRepository;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.member.user.domain.Member;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.user.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;

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


    //멤버 즐겨찾기 조회
    public List<BookmarkSearchMemberResponse> searchMemberBookmark(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(memberId, null);
        List<BookmarkSearchMemberResponse> result = bookmarks.stream()
                .map(b -> BookmarkSearchMemberResponse.from(b))
                .collect(Collectors.toList());

        return result;
    }
}
