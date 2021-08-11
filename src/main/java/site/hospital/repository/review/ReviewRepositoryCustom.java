package site.hospital.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.review.Review;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;
import site.hospital.dto.review.StaffSearchReviewDTO;

import java.util.List;


public interface ReviewRepositoryCustom {
    List<Review> hospitalReviewSearch(Long hospitalId, Long memberId);
    Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable);
    Review viewHospitalReview(Long reviewId);
    Page<Review> staffSearchReviews(Long hospitalId, StaffReviewSearchCondition condition, Pageable pageable);
    void adminDeleteReviewHospital(Hospital hospital);
}
