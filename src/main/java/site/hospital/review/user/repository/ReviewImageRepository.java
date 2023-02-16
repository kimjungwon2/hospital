package site.hospital.review.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.review.user.domain.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {

}
