package site.hospital.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.tag.domain.PostTag;
import site.hospital.tag.domain.Tag;
import site.hospital.hospital.user.domain.Hospital;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagRepositoryCustom {

    PostTag findByTagAndHospital(Tag tag, Hospital hospital);
}
