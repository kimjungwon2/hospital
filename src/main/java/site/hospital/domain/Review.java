package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private long id;

    private String picture;

    //인증 상태[NONE, WAITING,CERTIFIED]
    @Enumerated(EnumType.STRING)
    private ReviewAuthentication authenticationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy="review", cascade = CascadeType.ALL)
    private List<ReviewHospital> reviewHospitals = new ArrayList<>();

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member){
        this.member = member;
        member.getReviews().add(this);
    }
    public void addReviewHospital(ReviewHospital reviewHospital){
        reviewHospitals.add(reviewHospital);
        reviewHospital.setReview(this);
    }

    /*
    생성 메서드
     */
    public Review(String picture){
        this.picture = picture;
        if(picture == null) this.authenticationStatus = authenticationStatus.NONE;
        else this.authenticationStatus = authenticationStatus.WAITING;
    }

    public static Review createReview(String picture, Member member, ReviewHospital... reviewHospitals){
        Review review = new Review(picture);

        review.changeMember(member);
        for (ReviewHospital reviewHospital : reviewHospitals) {
            review.addReviewHospital(reviewHospital);
        }

        return review;
    }

}
