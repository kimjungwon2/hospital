package site.hospital.review.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;
import site.hospital.member.user.domain.Member;
import site.hospital.review.user.domain.reviewhospital.ReviewHospital;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReviewAuthentication authenticationStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_image_id")
    private ReviewImage reviewImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewHospital> reviewHospitals = new ArrayList<>();
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member) {
        this.member = member;
        member.getReviews().add(this);
    }

    public void addReviewHospital(ReviewHospital reviewHospital) {
        reviewHospitals.add(reviewHospital);
        reviewHospital.setReview(this);
    }

    public void changeReviewImage(ReviewImage reviewImage) {
        this.reviewImage = reviewImage;
        reviewImage.setReview(this);
    }


    public void changeAuthenticationStatus(ReviewAuthentication authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public void approveReviewCertification(ReviewAuthentication authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    /*
        생성 메서드
    */
    public Review(ReviewAuthentication authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public static Review createReview(Member member, ReviewHospital... reviewHospitals) {
        Review review = new Review(ReviewAuthentication.NONE);
        review.changeMember(member);

        for (ReviewHospital reviewHospital : reviewHospitals) {
            review.addReviewHospital(reviewHospital);
        }

        return review;
    }

}
