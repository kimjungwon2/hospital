package site.hospital.repository.postTag;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.PostTag;
import site.hospital.domain.Tag;
import site.hospital.domain.hospital.Hospital;

public interface PostTagRepository extends JpaRepository<PostTag, Long>, PostTagRepositoryCustom {

    PostTag findByTagAndHospital(Tag tag, Hospital hospital);
}
