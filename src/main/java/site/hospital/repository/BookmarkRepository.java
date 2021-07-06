package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
}
