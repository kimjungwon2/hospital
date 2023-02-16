package site.hospital.review.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseTimeEntity;
import site.hospital.member.user.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //생성 메서드
    public static ReviewLike createReviewLike(Member member, Review review) {
        ReviewLike reviewLike = new ReviewLike();
        reviewLike.changeMember(member);
        reviewLike.changeReview(review);

        return reviewLike;
    }

    //== 연관 관계 메서드 ==/
    public void changeMember(Member member) {
        this.member = member;
        member.getReviewLikes().add(this);
    }

    public void changeReview(Review review) {
        this.review = review;
        review.getReviewLikes().add(this);
    }

}
