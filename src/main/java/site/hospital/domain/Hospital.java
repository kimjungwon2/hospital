package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    @OneToMany(mappedBy = "hospital")
    private List<Estimation> estimations = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<PostTag> postTags = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<Appointment> appointments = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<Bookmark> bookmarks = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detailed_hos_information_id")
    private DetailedHosInformation detailedHosInformation;

    //인허가 날짜
    private String licensingDate;
    private String hospitalName;
    private String phoneNumber;
    private String distinguishedName;
    private String medicalSubject;
    private String medicalSubjectInformation;
    private String businessCondition;
    private String cityName;

    private int numberHealthcareProvider;
    private int numberWard;
    //입원실 수
    private int numberPatientRoom;

    @Embedded
    private HospitalAddress hospitalAddress;

    //병원 장소(좌표)
    @Embedded
    private HospitalLocation hospitalLocation;

    /*
    생성자
    */
    @Builder
    public Hospital(Long id, String licensingDate, String hospitalName,
                    String phoneNumber, String distinguishedName, String medicalSubject,
                    String medicalSubjectInformation, String businessCondition, String cityName,
                    int numberHealthcareProvider, int numberWard, int numberPatientRoom,
                    HospitalAddress hospitalAddress, HospitalLocation hospitalLocation) {
        this.id = id;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubject = medicalSubject;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
        this.numberHealthcareProvider = numberHealthcareProvider;
        this.numberWard = numberWard;
        this.numberPatientRoom = numberPatientRoom;
        this.hospitalAddress = hospitalAddress.builder()
                .roadBaseAddress(hospitalAddress.getRoadBaseAddress())
                .landLotBasedSystem(hospitalAddress.getLandLotBasedSystem())
                .zipCode(hospitalAddress.getZipCode())
                .build();
        this.hospitalLocation = hospitalLocation.builder()
                .latitude(hospitalLocation.getLatitude())
                .longitude(hospitalLocation.getLongitude())
                .xCoordination(hospitalLocation.getXCoordination())
                .yCoordination(hospitalLocation.getYCoordination())
                .build();
    }
}
