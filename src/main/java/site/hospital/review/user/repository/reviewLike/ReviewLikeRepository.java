package site.hospital.review.user.repository.reviewLike;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.review.user.domain.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long>,
        ReviewLikeRepositoryCustom {

}
