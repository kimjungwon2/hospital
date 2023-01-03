package site.hospital.repository.reviewLike;

import org.springframework.data.jpa.repository.JpaRepository;
import site.hospital.domain.ReviewLike;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long>,
        ReviewLikeRepositoryCustom {

}
