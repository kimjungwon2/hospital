package site.hospital.repository.bookmark;

import site.hospital.domain.Bookmark;

import java.util.List;

public interface BookmarkRepositoryCustom {
    public List<Bookmark> searchBookmark(Long memberId, Long hospitalId);
}
