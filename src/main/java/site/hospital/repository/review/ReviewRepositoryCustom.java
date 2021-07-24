package site.hospital.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import site.hospital.domain.review.Review;
import site.hospital.dto.ReviewSearchCondition;
import site.hospital.repository.member.simplequery.MemberSearchCondition;

import java.util.List;


public interface ReviewRepositoryCustom {
    public List<Review> hospitalReviewSearch(Long hospitalId, Long memberId, Long reviewId);
    public List<Review> adminReviews(int offset, int limit);
}
