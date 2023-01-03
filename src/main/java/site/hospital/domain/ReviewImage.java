package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.review.Review;

import javax.persistence.*;

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

    //연관관계 때문에 set 설정
    public void setReview(Review review) {
        this.review = review;
    }

    @Builder
    public ReviewImage(String originalName, String imageKey) {
        this.originalName = originalName;
        this.imageKey = imageKey;
    }
}
