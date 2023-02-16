package site.hospital.tag.manager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.tag.manager.domain.Tag;
import site.hospital.hospital.user.domain.Hospital;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagRepositoryCustom {

    PostTag findByTagAndHospital(Tag tag, Hospital hospital);
}
