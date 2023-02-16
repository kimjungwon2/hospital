package site.hospital.review.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.review.user.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

}
