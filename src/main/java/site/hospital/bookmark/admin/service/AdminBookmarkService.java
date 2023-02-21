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

    public List<BookmarkSearchResponse> adminSearchBookmarkUsers() {
        List<Bookmark> bookmarks = bookmarkRepository.searchBookmark(null, null);

        return bookmarks
                .stream()
                .map(bookmark -> BookmarkSearchResponse.from(bookmark))
                .collect(Collectors.toList());
    }
}
