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
public class Detailedhosinformation extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "detailedhosinformation_id")
    private long id;

    @OneToOne(mappedBy = "detailedhosinformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @OneToMany(mappedBy = "detailedhosinformation")
    private List<Doctor> doctors = new ArrayList<>();

    private String photo;
    private String introduction;
    private String consultationHour;
    private String abnormality;

}
