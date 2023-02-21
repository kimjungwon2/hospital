package site.hospital.bookmark.user.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.bookmark.user.repository.dto.ManagerBookmarkSearchCondition;

public interface BookmarkRepositoryCustom {

    Bookmark userCheckBookmark(Long memberId, Long hospitalId);

    Page<Bookmark> managerSearchBookmarkUsers(Long hospitalId, ManagerBookmarkSearchCondition condition,
            Pageable pageable);

    List<Bookmark> searchBookmark(Long memberId, Long hospitalId);
}
