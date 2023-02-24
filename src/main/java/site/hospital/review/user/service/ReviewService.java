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
import site.hospital.review.user.repository.query.ReviewSearchDto;
import site.hospital.review.user.repository.query.ReviewSearchRepository;
import site.hospital.review.user.repository.reviewLike.ReviewLikeRepository;

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

    //리뷰 등록
    @Transactional
    public ReviewCreateResponse reviewRegister(
            MultipartFile imageFile,
            ReviewCreateRequest requestData
    ) throws IOException {
        EvaluationCriteria evaluationCriteria = EvaluationCriteria.builder()
                .sumPrice(requestData.getSumPrice()).kindness(requestData.getKindness())
                .symptomRelief(requestData.getSymptomRelief())
                .cleanliness(requestData.getCleanliness())
                .waitTime(requestData.getWaitTime()).build();

        ReviewHospital reviewHospital = ReviewHospital.builder()
                .content(requestData.getContent()).disease(requestData.getDisease())
                .recommendationStatus(requestData.getRecommendationStatus())
                .evCriteria(evaluationCriteria)
                .build();

        Member member = memberRepository.findById(requestData.getMemberId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(requestData.getHospitalId())
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //리뷰 병원 생성
        ReviewHospital createdReviewHospital = ReviewHospital
                .saveReviewHospital(hospital, reviewHospital);

        //리뷰 생성
        Review review = Review.createReview(member, createdReviewHospital);
        reviewRepository.save(review);

        //영수증 파일 저장.
        if (imageFile != null) {
            reviewReceiptImageService.uploadImage(imageFile, review.getId());
        }

        return ReviewCreateResponse.from(review.getId());
    }

    //리뷰 좋아요 여부 확인.
    public ReviewConfirmLikeResponse isLikeReview(Long memberId, Long reviewId) {
        Boolean isLikeReview = false;
        ReviewLike reviewLike = reviewLikeRepository.isLikeReview(memberId, reviewId);

        if (reviewLike != null) {
            isLikeReview = true;
        }

        return ReviewConfirmLikeResponse.from(isLikeReview);
    }

    //리뷰 좋아요 + 취소하기
    @Transactional
    public void likeReview(Long memberId, Long reviewId) {
        Boolean existence = false;

        //리뷰에 좋아요 있는지 확인.
        ReviewLike isLike = reviewLikeRepository.isLikeReview(memberId, reviewId);

        //리뷰에 좋아요 존재 시, true
        if (isLike != null) {
            existence = true;
        }

        //좋아요가 있으면 삭제.
        if (existence == true) {
            reviewLikeRepository.delete(isLike);
        }
        //좋아요 데이터가 없으면 저장.
        else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

            ReviewLike reviewLike = ReviewLike.createReviewLike(member, review);
            reviewLikeRepository.save(reviewLike);
        }

    }

    //리뷰 인증 승인(관리자)
    @Transactional
    public void approval(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approve();
    }

    //병원에 등록된 리뷰 검색
    public List<ReviewViewListsResponse> hospitalReviewList(Long hospitalId) {
        List<Review> review = reviewRepository.hospitalReviewSearch(hospitalId, null);

        List<ReviewViewListsResponse> result = review.stream()
                .map(r -> ReviewViewListsResponse.from(r))
                .collect(Collectors.toList());

        return result;
    }

    //유저가 등록한 리뷰 검색
    public List<ReviewViewByMemberResponse> userReviewSearch(Long memberId) {
        List<Review> review = reviewRepository.hospitalReviewSearch(null, memberId);

        List<ReviewViewByMemberResponse> result = review.stream()
                .map(r -> ReviewViewByMemberResponse.from(r))
                .collect(Collectors.toList());

        return result;
    }

    //병원 리뷰 전체 검색
    public Page<ReviewSearchDto> searchReview(String searchName, Pageable pageable) {
        return reviewSearchRepository.searchReview(searchName, pageable);
    }

    //리뷰 상세 보기
    public ReviewViewDetailResponse viewHospitalReview(Long reviewId) {
        Review review = reviewRepository.viewHospitalReview(reviewId);
        return ReviewViewDetailResponse.from(review);
    }

}
