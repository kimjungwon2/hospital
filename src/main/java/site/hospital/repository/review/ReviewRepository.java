package site.hospital.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

}
