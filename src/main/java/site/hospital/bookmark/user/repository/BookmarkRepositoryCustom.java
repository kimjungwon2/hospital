package site.hospital.bookmark.user.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.dto.StaffBookmarkSearchCondition;

public interface BookmarkRepositoryCustom {

    Bookmark isUserBookmark(Long memberId, Long hospitalId);

    Page<Bookmark> staffSearchBookmark(Long hospitalId, StaffBookmarkSearchCondition condition,
            Pageable pageable);

    List<Bookmark> searchBookmark(Long memberId, Long hospitalId);
}
