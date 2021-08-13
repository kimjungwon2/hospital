package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage,Long> {
}
