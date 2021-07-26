package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Tag;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findByName(String name);
    Tag findOneByName(String name);
}
