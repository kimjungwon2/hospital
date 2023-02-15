package site.hospital.hospital.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    Hospital hospital;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_image_id")
    private Long id;
    @Column(unique = true, nullable = false)
    private String imageKey;

    private String originalName;

    @Builder
    public HospitalImage(String originalName, String imageKey, Hospital hospital) {
        this.originalName = originalName;
        this.imageKey = imageKey;
        this.hospital = hospital;
    }

    //연관 관계 때문에 설정.
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
