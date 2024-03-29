package site.hospital.review.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id")
    private long id;

    private String originalName;

    @Column(unique = true, nullable = false)
    private String imageKey;

    @OneToOne(mappedBy = "reviewImage", fetch = FetchType.LAZY)
    private Review review;

    @Builder
    public ReviewImage(String originalName, String imageKey) {
        this.originalName = originalName;
        this.imageKey = imageKey;
    }

    //연관관계 때문에 set 설정
    public void setReview(Review review) {
        this.review = review;
    }
}
