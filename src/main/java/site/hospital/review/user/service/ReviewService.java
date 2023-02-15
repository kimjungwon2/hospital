package site.hospital.review.user.service;

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
import site.hospital.common.service.ImageManagementService;
import site.hospital.common.service.JwtStaffAccessService;
import site.hospital.review.user.api.dto.ReviewConfirmLikeResponse;
import site.hospital.review.user.api.dto.ReviewCreateRequest;
import site.hospital.review.user.api.dto.ReviewCreateResponse;
import site.hospital.review.user.api.dto.member.ReviewViewByMemberResponse;
import site.hospital.review.user.api.dto.searchReviews.ReviewSearchListsResponse;
import site.hospital.review.user.api.dto.viewDetail.ReviewViewDetailResponse;
import site.hospital.review.user.api.dto.viewLists.ReviewViewListsResponse;
import site.hospital.review.user.domain.ReviewLike;
import site.hospital.hospital.user.domain.Hospital;
import site.hospital.member.user.domain.Member;
import site.hospital.review.user.domain.Review;
import site.hospital.review.user.domain.ReviewAuthentication;
import site.hospital.review.user.domain.reviewHospital.EvaluationCriteria;
import site.hospital.review.user.domain.reviewHospital.ReviewHospital;
import site.hospital.review.admin.repository.dto.AdminReviewSearchCondition;
import site.hospital.review.user.repository.dto.StaffReviewSearchCondition;
import site.hospital.hospital.user.repository.HospitalRepository;
import site.hospital.member.user.repository.MemberRepository;
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
