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
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.user.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    //관리자 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

        reviewRepository.deleteById(reviewId);
    }

    //관리자 리뷰 승인해주기
    @Transactional
    public void approve(Long reviewId, ReviewAuthentication reviewAuthentication) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approveCertification(reviewAuthentication);
    }

    //관리자 승인 대기 리뷰 검색
    public Page<ReviewSearchListsResponse> adminSearchUnapprovedReviews(Pageable pageable) {
        Page<Review> reviews = reviewRepository.adminSearchUnapprovedReviews(pageable);
        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

    //관리자 미승인 리뷰 갯수
    public Long adminUnapprovedReviewCount() {
        return reviewRepository.adminUnapprovedReviewCount();
    }



    //관리자 리뷰 검색
    public Page<ReviewSearchListsResponse> adminSearchReviews(
            String nickName,
            String hospitalName,
            String memberIdName,
            Pageable pageable
    ) {
        AdminReviewSearchCondition condition = AdminReviewSearchCondition.builder()
                .nickName(nickName).hospitalName(hospitalName).memberIdName(memberIdName).build();

        Page<Review> reviews = reviewRepository.adminSearchReviews(condition, pageable);

        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl(result, pageable, total);
    }

}
