package site.hospital.review.user.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.common.service.image.ReviewReceiptImageService;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.review.user.api.dto.ReviewConfirmLikeResponse;
import site.hospital.review.user.api.dto.ReviewCreateRequest;
import site.hospital.review.user.api.dto.ReviewCreateResponse;
import site.hospital.review.user.api.dto.member.ReviewViewByMemberResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.api.dto.viewLists.ReviewViewListsResponse;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewLike;
import site.hospital.review.user.domain.reviewHospital.EvaluationCriteria;
import site.hospital.review.user.domain.reviewHospital.ReviewHospital;
import site.hospital.review.user.repository.ReviewRepository;
import site.hospital.review.user.repository.search.ReviewSearchSelectQuery;
import site.hospital.review.user.repository.search.ReviewSearchRepository;
import site.hospital.review.user.repository.reviewlike.ReviewLikeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final ReviewSearchRepository reviewSearchRepository;
    private final ReviewReceiptImageService reviewReceiptImageService;

    @Transactional
    public ReviewCreateResponse registerReview(
            MultipartFile imageFile,
            ReviewCreateRequest requestData
    ) throws IOException {

        Member member = memberRepository.findById(requestData.getMemberId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(requestData.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        ReviewHospital reviewHospital = createReviewHospital(requestData, hospital);
        Review review = createReview(member, reviewHospital);
        checkReceiptImage(imageFile, review);

        return ReviewCreateResponse.from(review.getId());
    }

    public ReviewConfirmLikeResponse checkReviewLike(Long memberId, Long reviewId) {
        ReviewLike reviewLike = reviewLikeRepository.checkReviewLike(memberId, reviewId);

        return ReviewConfirmLikeResponse.from(checkLike(reviewLike));
    }

    @Transactional
    public void likeReview(Long memberId, Long reviewId) {
        ReviewLike reviewLike = reviewLikeRepository.checkReviewLike(memberId, reviewId);

        Boolean isReviewLike = checkLike(reviewLike);

        if (isReviewLike == true) {
            deleteReviewLike(reviewLike);
        }
        else {
            registerReviewLike(memberId, reviewId);
        }
    }

    public List<ReviewViewListsResponse> viewHospitalReviews(Long hospitalId) {
        List<Review> reviews = reviewRepository.searchHospitalReviews(hospitalId, null);

        List<ReviewViewListsResponse> reviewLists =
                reviews
                .stream()
                .map(r -> ReviewViewListsResponse.from(r))
                .collect(Collectors.toList());

        return reviewLists;
    }

    public List<ReviewViewByMemberResponse> viewReviewsByUser(Long memberId) {
        List<Review> review = reviewRepository.searchHospitalReviews(null, memberId);

        List<ReviewViewByMemberResponse> reviewListsByUser =
                review
                .stream()
                .map(r -> ReviewViewByMemberResponse.from(r))
                .collect(Collectors.toList());

        return reviewListsByUser;
    }

    public Page<ReviewSearchSelectQuery> searchReviews(String searchName, Pageable pageable) {
        return reviewSearchRepository.searchReviews(searchName, pageable);
    }

    public ReviewViewDetailResponse viewDetailedReview(Long reviewId) {
        Review review = reviewRepository.viewHospitalReview(reviewId);
        return ReviewViewDetailResponse.from(review);
    }

    private ReviewHospital createReviewHospital(ReviewCreateRequest requestData, Hospital hospital) {
        EvaluationCriteria evaluationCriteria = EvaluationCriteria.builder()
                .sumPrice(requestData.getSumPrice()).kindness(requestData.getKindness())
                .symptomRelief(requestData.getSymptomRelief())
                .cleanliness(requestData.getCleanliness())
                .waitTime(requestData.getWaitTime()).build();

        ReviewHospital reviewHospital = ReviewHospital
                .builder()
                .content(requestData.getContent())
                .disease(requestData.getDisease())
                .recommendationStatus(requestData.getRecommendationStatus())
                .evCriteria(evaluationCriteria)
                .build();

        ReviewHospital createdReviewHospital = ReviewHospital.saveReviewHospital(hospital, reviewHospital);
        return createdReviewHospital;
    }

    private void checkReceiptImage(MultipartFile receiptImage, Review review) throws IOException {
        if (receiptImage != null) {
            reviewReceiptImageService.uploadImage(receiptImage, review.getId());
        }
    }

    private Review createReview(Member member, ReviewHospital createdReviewHospital) {
        Review review = Review.createReview(member, createdReviewHospital);
        reviewRepository.save(review);
        return review;
    }

    private Boolean checkLike(ReviewLike reviewLike) {
        Boolean isReviewLike = false;

        if (reviewLike != null) {
            isReviewLike = true;
        }
        return isReviewLike;
    }

    private void registerReviewLike(Long memberId, Long reviewId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

        ReviewLike reviewLike = ReviewLike.createReviewLike(member, review);
        reviewLikeRepository.save(reviewLike);
    }

    private void deleteReviewLike(ReviewLike reviewLike) {
        reviewLikeRepository.delete(reviewLike);
    }

}
