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
public class Hospital extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "hospital_id")
    private Long id;

    @OneToMany(mappedBy = "hospital")
    private List<Estimation> estimations = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<PostTag> posttags = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detailedhosinformation_id")
    private Detailedhosinformation detailedhosinformation;

    //인허가 날짜
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubject;
    private String medicalSubjectInformation;
    private String businessCondition;
    private String city;

    private int numberHealthcareProvider;
    private int numberWard;
    //입원실 수
    private int numberPatientRoom;

    @Embedded
    private HospitalAddress hospitalAddress;

    //병원 장소(좌표)
    @Embedded
    private HospitalLocation hospitalLocation;

}
