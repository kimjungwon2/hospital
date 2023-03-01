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
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;


    @Override
    public BookmarkCheckResponse userCheckBookmark(Long memberId, Long hospitalId) {
        Bookmark checkBookmark = bookmarkRepository.userCheckBookmark(memberId, hospitalId);
        Boolean bookmarkExistence = checkBookmark(checkBookmark);

        return BookmarkCheckResponse.from(bookmarkExistence);
    }

    @Transactional
    @Override
    public void userRegisterBookmark(BookmarkCreateRequest request) {

        Bookmark bookmark = bookmarkRepository
                .userCheckBookmark(request.getMemberId(), request.getHospitalId());

        if (checkBookmark(bookmark)) {
            deleteBookmark(bookmark);
        }
        else {
            saveBookmark(request);
        }
    }

    @Override
    public List<BookmarkSearchMemberResponse> searchUserBookmarks(Long memberId) {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(memberId, null);

        return bookmarks
                .stream()
                .map(bookmark -> BookmarkSearchMemberResponse.from(bookmark))
                .collect(Collectors.toList());
    }

    private void deleteBookmark(Bookmark checkBookmark) {
        bookmarkRepository.delete(checkBookmark);
    }

    private void saveBookmark(BookmarkCreateRequest request) {
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

    private boolean checkBookmark(Bookmark bookmark) {

        boolean bookmarkPresence = false;

        if (bookmark != null) {
            bookmarkPresence = true;
        }

        return bookmarkPresence;
    }

}
