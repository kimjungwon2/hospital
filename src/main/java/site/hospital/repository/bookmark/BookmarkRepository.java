package site.hospital.repository.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long>,BookmarkRepositoryCustom {
}
