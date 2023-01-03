package site.hospital.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findByName(String name);

    Page<Tag> findOneByName(String name, Pageable pagealbe);
}
