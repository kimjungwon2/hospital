package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.*;
import site.hospital.domain.member.Member;
import site.hospital.domain.reviewHospital.Recommendation;
import site.hospital.domain.review.Review;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.repository.HospitalRepository;
import site.hospital.repository.member.MemberRepository;
import site.hospital.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final HospitalRepository hospitalRepository;

    //리뷰 등록
    @Transactional
    public Long reviewRegister(Long memberId, Long hospitalId, String picture, String content, String disease,
                               Recommendation recommendationStatus,
                               int sumPrice, int kindness, int symptomRelief,
                               int cleanliness, int waitTime){

        Member member = memberRepository.findById(memberId).orElse(null);
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);

        //리뷰 병원 생성
        ReviewHospital reviewHospital = ReviewHospital.createReviewHospital(hospital, content, disease,
                recommendationStatus, sumPrice, kindness, symptomRelief, cleanliness, waitTime);


        //리뷰 생성
        Review review = Review.createReview(picture, member, reviewHospital);

        reviewRepository.save(review);

        return review.getId();
    }

}
