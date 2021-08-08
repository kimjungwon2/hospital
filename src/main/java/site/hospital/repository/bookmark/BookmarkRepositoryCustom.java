package site.hospital.repository.bookmark;

import site.hospital.domain.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {
    Bookmark isUserBookmark(Long memberId,Long hospitalId);
    List<Bookmark> searchBookmark(Long memberId, Long hospitalId);
}
