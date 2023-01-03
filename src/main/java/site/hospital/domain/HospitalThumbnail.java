package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.hospital.Hospital;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalThumbnail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospitalThumbnail_id")
    private long id;

    private String originalName;

    @Column(unique = true, nullable = false)
    private String imageKey;

    @OneToOne(mappedBy = "hospitalThumbnail", fetch = FetchType.LAZY)
    private Hospital hospital;

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Builder
    public HospitalThumbnail(String originalName, String imageKey) {
        this.originalName = originalName;
        this.imageKey = imageKey;
    }
}
