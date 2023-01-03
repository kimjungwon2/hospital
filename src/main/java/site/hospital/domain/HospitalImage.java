package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.hospital.Hospital;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    Hospital hospital;

    @Column(unique = true, nullable = false)
    private String imageKey;

    private String originalName;

    //연관 관계 때문에 설정.
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Builder
    public HospitalImage(String originalName, String imageKey, Hospital hospital) {
        this.originalName = originalName;
        this.imageKey = imageKey;
        this.hospital = hospital;
    }
}
