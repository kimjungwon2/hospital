package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.HospitalThumbnail;

public interface HospitalThumbnailRepository extends JpaRepository<HospitalThumbnail, Long> {

}
