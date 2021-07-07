package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseTimeEntity;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;

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
    @OneToMany(mappedBy = "hospital")
    private List<QandA> qandAs = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffHosInformation_id")
    private StaffHosInformation staffHosInformation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detailedHosInformation_id")
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


    //== 연관 관계 메서드 ==//
    public void changeStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
        staffHosInformation.setHospital(this);
    }

    public void changeDetailedHosInformation(DetailedHosInformation detailedHosInformation){
        this.detailedHosInformation = detailedHosInformation;
        detailedHosInformation.setHospital(this);
    }

    /*생성자*/

    //병원 생성
    @Builder
    public Hospital(Long id, String licensingDate, String hospitalName,
                    String phoneNumber, String distinguishedName, String medicalSubject,
                    String medicalSubjectInformation, String businessCondition, String cityName) {
        this.id = id;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubject = medicalSubject;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }

    /* 생성 메서드*/
    //병원 상세 정보 등록
    public static Hospital createDetailedHosInformation(DetailedHosInformation detailedHosInformation){
        Hospital hospital = new Hospital();
        hospital.changeDetailedHosInformation(detailedHosInformation);

        return hospital;
    }

    //병원 추가 정보 등록(직원)
    public static Hospital createStaffHosInformation(StaffHosInformation staffHosInformation){
        Hospital hospital = new Hospital();
        hospital.changeStaffHosInformation(staffHosInformation);

        return hospital;
    }



}
