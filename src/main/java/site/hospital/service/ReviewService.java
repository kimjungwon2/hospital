package site.hospital.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.hospital.api.dto.review.ReviewConfirmLikeResponse;
import site.hospital.api.dto.review.ReviewCreateRequest;
import site.hospital.api.dto.review.ReviewCreateResponse;
import site.hospital.api.dto.review.member.ReviewViewByMemberResponse;
import site.hospital.api.dto.review.searchReviews.ReviewSearchListsResponse;
import site.hospital.api.dto.review.viewDetail.ReviewViewDetailResponse;
import site.hospital.api.dto.review.viewLists.ReviewViewListsResponse;
import site.hospital.domain.ReviewLike;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.domain.review.Review;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.domain.reviewHospital.EvaluationCriteria;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.review.ReviewRepository;
import site.hospital.repository.review.query.ReviewSearchDto;
import site.hospital.repository.review.query.ReviewSearchRepository;
import site.hospital.repository.reviewLike.ReviewLikeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final ReviewSearchRepository reviewSearchRepository;
    private final JwtStaffAccessService jwtStaffAccessService;
    private final ImageManagementService imageManagementService;

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
            imageManagementService.reviewReceiptUpload(imageFile, "receipt", review.getId());
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

    //병원 관계자 리뷰 검색
    public Page<ReviewSearchListsResponse> staffSearchReviews(
            ServletRequest servletRequest,
            String nickName,
            String memberIdName,
            Pageable pageable
    ) {
        StaffReviewSearchCondition condition = StaffReviewSearchCondition.builder()
                .nickName(nickName).memberIdName(memberIdName).build();

        Long hospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);
        Page<Review> reviews = reviewRepository.staffSearchReviews(hospitalId, condition, pageable);

        List<ReviewSearchListsResponse> result = reviews.stream()
                .map(r -> ReviewSearchListsResponse.from(r))
                .collect(Collectors.toList());

        Long total = reviews.getTotalElements();

        return new PageImpl(result, pageable, total);
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

    //관리자 미승인 리뷰 갯수
    public Long adminUnapprovedReviewCount() {
        return reviewRepository.adminUnapprovedReviewCount();
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

    //관리자 리뷰 승인해주기
    @Transactional
    public void approve(Long reviewId, ReviewAuthentication reviewAuthentication) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approveCertification(reviewAuthentication);
    }

    //리뷰 상세 보기
    public ReviewViewDetailResponse viewHospitalReview(Long reviewId) {
        Review review = reviewRepository.viewHospitalReview(reviewId);
        return ReviewViewDetailResponse.from(review);
    }

    //관리자 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

        reviewRepository.deleteById(reviewId);
    }
}
