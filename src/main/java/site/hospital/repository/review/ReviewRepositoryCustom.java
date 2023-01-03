package site.hospital.repository.review;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.review.Review;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;


public interface ReviewRepositoryCustom {

    List<Review> hospitalReviewSearch(Long hospitalId, Long memberId);

    Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable);

    Review viewHospitalReview(Long reviewId);

    Page<Review> staffSearchReviews(Long hospitalId, StaffReviewSearchCondition condition,
            Pageable pageable);

    Page<Review> adminSearchUnapprovedReviews(Pageable pageable);

    Long adminUnapprovedReviewCount();

    void adminDeleteReviewHospital(Hospital hospital);
}
