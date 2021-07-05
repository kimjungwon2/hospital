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
public class DetailedHosInformation extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailed_hos_information_id")
    private long id;

    @OneToOne(mappedBy = "detailedHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "detailedHosInformation")
    private List<Doctor> doctors = new ArrayList<>();

    private String photo;
    private String introduction;
    private String consultationHour;
    private String abnormality;

}
