package site.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
