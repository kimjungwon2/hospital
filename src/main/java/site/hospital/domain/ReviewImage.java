package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.review.Review;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_image_id")
    private long id;

    @OneToOne(mappedBy = "reviewImage", fetch = FetchType.LAZY)
    private Review review;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String filePath;

    private long fileSize;

}
