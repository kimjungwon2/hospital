package site.hospital.review.admin.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.review.admin.repository.dto.AdminReviewSearchCondition;
import site.hospital.review.user.api.dto.search.ReviewSearchListsResponse;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.user.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminReviewServiceImpl implements AdminReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

        reviewRepository.deleteById(reviewId);
    }

    @Transactional
    @Override
    public void approveReview(Long reviewId, ReviewAuthentication reviewAuthentication) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approveReviewCertification(reviewAuthentication);
    }

    @Override
    public Page<ReviewSearchListsResponse> searchUnapprovedReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.adminSearchUnapprovedReviews(pageable);

        List<ReviewSearchListsResponse> searchResults =
                reviews
                        .stream()
                        .map(r -> ReviewSearchListsResponse.from(r))
                        .collect(Collectors.toList());

        Long totalCounts = reviews.getTotalElements();

        return new PageImpl(searchResults, pageable, totalCounts);
    }

    @Override
    public Long countUnapprovedReviews() {
        return reviewRepository.adminCountUnapprovedReview();
    }

    @Override
    public Page<ReviewSearchListsResponse> searchReviews(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        Page<Review> reviews = getSearchReviews(nickName, hospitalName, memberIdName, pageable);

        List<ReviewSearchListsResponse> searchResults =
                reviews
                        .stream()
                        .map(r -> ReviewSearchListsResponse.from(r))
                        .collect(Collectors.toList());

        Long totalCounts = reviews.getTotalElements();

        return new PageImpl(searchResults, pageable, totalCounts);
    }

    private Page<Review> getSearchReviews(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        AdminReviewSearchCondition condition =
                AdminReviewSearchCondition
                        .builder()
                        .nickName(nickName)
                        .hospitalName(hospitalName)
                        .memberIdName(memberIdName)
                        .build();

        Page<Review> reviews = reviewRepository.adminSearchReviews(condition, pageable);
        return reviews;
    }
}
