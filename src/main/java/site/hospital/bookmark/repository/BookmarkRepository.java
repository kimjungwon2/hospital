package site.hospital.bookmark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.bookmark.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>,
        BookmarkRepositoryCustom {

}
