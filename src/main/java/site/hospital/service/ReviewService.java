package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.hospital.Hospital;
import site.hospital.domain.member.Member;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.domain.review.Review;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.domain.ReviewLike;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.dto.StaffReviewSearchCondition;
import site.hospital.repository.reviewLike.ReviewLikeRepository;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.review.ReviewRepository;
import site.hospital.repository.review.query.ReviewSearchDto;
import site.hospital.repository.review.query.ReviewSearchRepository;

import javax.servlet.ServletRequest;
import java.util.List;

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

    //리뷰 등록
    @Transactional
    public Long reviewRegister(Long memberId, Long hospitalId, ReviewHospital reviewHospitalDTO){

        Member member = memberRepository.findById(memberId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 병원이 존재하지 않습니다."));

        //리뷰 병원 생성
        ReviewHospital reviewHospital = ReviewHospital.saveReviewHospital(hospital, reviewHospitalDTO);

        //리뷰 생성
        Review review = Review.createReview(member, reviewHospital);
        reviewRepository.save(review);

        return review.getId();
    }

    //리뷰 좋아요 여부 확인.
    public ReviewLike isLikeReview(Long memberId, Long reviewId){
        ReviewLike isLikeReview = reviewLikeRepository.isLikeReview(memberId, reviewId);

        return isLikeReview;
    }

    //리뷰 좋아요 + 취소하기
    @Transactional
    public void likeReview(Long memberId, Long reviewId){
        Boolean existence = false;

        //리뷰에 좋아요 있는지 확인.
        ReviewLike isLike = reviewLikeRepository.isLikeReview(memberId,reviewId);

        //리뷰에 좋아요 존재 시, true
        if(isLike!=null) existence = true;

        //좋아요가 있으면 삭제.
        if(existence== true) {
            reviewLikeRepository.delete(isLike);
        }
        //좋아요 데이터가 없으면 저장.
        else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(()->new IllegalStateException("해당 id에 속하는 멤버가 존재하지 않습니다."));
            Review review = reviewRepository.findById(reviewId)
                    .orElseThrow(()->new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

            ReviewLike reviewLike = ReviewLike.createReviewLike(member, review);
            reviewLikeRepository.save(reviewLike);
        }

    }

    //리뷰 인증 승인(관리자)
    @Transactional
    public void approval(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approve();
    }

    //병원에 등록된 리뷰 검색
    public List<Review> hospitalReviewList(Long hospitalId){
        return reviewRepository.hospitalReviewSearch(hospitalId,null);
    }

    //유저가 등록한 리뷰 검색
    public List<Review> userReviewSearch(Long memberId){
        return reviewRepository.hospitalReviewSearch(null,memberId);
    }

    //병원 리뷰 전체 검색
    public Page<ReviewSearchDto> searchReview(String searchName, Pageable pageable){
        return reviewSearchRepository.searchReview(searchName, pageable);
    }

    //병원 관계자 리뷰 검색
    public Page<Review> staffSearchReviews(ServletRequest servletRequest, StaffReviewSearchCondition condition, Pageable pageable){
        Long hospitalId = jwtStaffAccessService.getHospitalNumber(servletRequest);
        return reviewRepository.staffSearchReviews(hospitalId, condition, pageable);
    }

    //관리자 리뷰 검색
    public Page<Review> adminSearchReviews(AdminReviewSearchCondition condition, Pageable pageable){
        return reviewRepository.adminSearchReviews(condition, pageable);
    }

    //관리자 리뷰 승인해주기
    @Transactional
    public void approve(Long reviewId, ReviewAuthentication reviewAuthentication){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));
        review.approveCertification(reviewAuthentication);
    }

    //리뷰 상세 보기
    public Review viewHospitalReview(Long reviewId){
        return reviewRepository.viewHospitalReview(reviewId);
    }

    //관리자 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new IllegalStateException("해당 id에 속하는 리뷰가 존재하지 않습니다."));

        reviewRepository.deleteById(reviewId);
    }
}
