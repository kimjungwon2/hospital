package site.hospital.bookmark.admin.service;


import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.bookmark.user.api.dto.BookmarkSearchResponse;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.BookmarkRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminBookmarkService {

    private final BookmarkRepository bookmarkRepository;

    //관리자 즐겨찾기 조회
    public List<BookmarkSearchResponse> searchAdminBookmark() {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(null, null);
        List<BookmarkSearchResponse> result =
                bookmarks.stream()
                        .map(b -> BookmarkSearchResponse.from(b))
                        .collect(Collectors.toList());

        return result;
    }
}
