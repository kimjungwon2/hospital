package site.hospital.hospital.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.hospital.user.domain.HospitalThumbnail;

public interface HospitalThumbnailRepository extends JpaRepository<HospitalThumbnail, Long> {

}
