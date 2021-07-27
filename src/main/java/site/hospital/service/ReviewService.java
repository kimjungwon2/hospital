package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.*;
import site.hospital.domain.member.Member;
import site.hospital.domain.review.ReviewAuthentication;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.review.Review;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.dto.AdminReviewSearchCondition;
import site.hospital.repository.hospital.HospitalRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.review.ReviewRepository;
import site.hospital.repository.review.query.ReviewSearchCondition;
import site.hospital.repository.review.query.ReviewSearchDto;
import site.hospital.repository.review.query.ReviewSearchRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;
    private final ReviewSearchRepository reviewSearchRepository;

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

    //리뷰 인증 승인(관리자)
    @Transactional
    public void approval(Long reviewId){
        Review review = reviewRepository.findById(reviewId).orElse(null);
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

    //리뷰 전체 검색
    public Page<ReviewSearchDto> searchReview(ReviewSearchCondition condition, Pageable pageable){
        return reviewSearchRepository.searchReview(condition, pageable);
    }

    //관리자 리뷰 조회
    public Page<Review> adminReviews(Pageable pageable){
        return reviewRepository.adminReviews(pageable);
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
