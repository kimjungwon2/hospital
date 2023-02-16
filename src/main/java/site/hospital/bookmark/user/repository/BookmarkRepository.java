package site.hospital.bookmark.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.bookmark.user.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>,
        BookmarkRepositoryCustom {

}
