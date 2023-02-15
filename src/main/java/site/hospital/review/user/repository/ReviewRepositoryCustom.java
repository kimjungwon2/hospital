package site.hospital.review.user.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.review.user.domain.Review;
import site.hospital.review.admin.repository.dto.AdminReviewSearchCondition;
import site.hospital.review.user.repository.dto.StaffReviewSearchCondition;


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
