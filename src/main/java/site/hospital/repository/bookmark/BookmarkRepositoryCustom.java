package site.hospital.repository.bookmark;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.Bookmark;
import site.hospital.dto.StaffBookmarkSearchCondition;

import java.util.List;

public interface BookmarkRepositoryCustom {

    Bookmark isUserBookmark(Long memberId, Long hospitalId);

    Page<Bookmark> staffSearchBookmark(Long hospitalId, StaffBookmarkSearchCondition condition,
            Pageable pageable);

    List<Bookmark> searchBookmark(Long memberId, Long hospitalId);
}
