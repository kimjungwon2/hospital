package site.hospital.domain.review;

import lombok.Getter;
import site.hospital.domain.ReviewImage;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.ReviewLike;
import site.hospital.domain.reviewHospital.ReviewHospital;
import site.hospital.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    //인증 상태[NONE, WAITING,CERTIFIED]
    @Enumerated(EnumType.STRING)
    private ReviewAuthentication authenticationStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_image_id")
    private ReviewImage reviewImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy="review", cascade = CascadeType.ALL)
    private List<ReviewHospital> reviewHospitals = new ArrayList<>();
    @OneToMany(mappedBy="review", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member){
        this.member = member;
        member.getReviews().add(this);
    }
    public void addReviewHospital(ReviewHospital reviewHospital){
        reviewHospitals.add(reviewHospital);
        reviewHospital.setReview(this);
    }
    public void changeReviewImage(ReviewImage reviewImage){
        this.reviewImage = reviewImage;
        reviewImage.setReview(this);
    }

    //리뷰 인증 승인
    public void approveCertification(ReviewAuthentication authenticationStatus){
        this.authenticationStatus = authenticationStatus;
    }

    /*
        생성 메서드
    */
    public Review(ReviewImage reviewImage){
        if(reviewImage == null) this.authenticationStatus = authenticationStatus.NONE;
        else this.authenticationStatus = authenticationStatus.WAITING;
    }

    public Review(){
    }

    public static Review createReview(Member member, ReviewHospital... reviewHospitals){
        Review review = new Review();

        review.changeMember(member);

        for (ReviewHospital reviewHospital : reviewHospitals) {
            review.addReviewHospital(reviewHospital);
        }

        return review;
    }

    public void approve(){
        this.authenticationStatus = authenticationStatus.CERTIFIED;
    }

}
