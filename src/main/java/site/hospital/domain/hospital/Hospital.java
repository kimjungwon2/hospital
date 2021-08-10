package site.hospital.domain.hospital;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.*;
import site.hospital.domain.appointment.Appointment;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.detailedHosInformation.DetailedHosInformation;
import site.hospital.domain.estimation.Estimation;
import site.hospital.domain.reviewHospital.ReviewHospital;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital extends BaseEntity {

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
    private List<Bookmark> bookmarks = new   ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<ReviewHospital> reviewHospitals = new ArrayList<>();

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
    private String medicalSubjectInformation;
    private String cityName;
    @Enumerated(EnumType.STRING)
    private BusinessCondition businessCondition;


    //== 연관 관계 메서드 ==//
    public void changeStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
        staffHosInformation.setHospital(this);
    }

    public void changeDetailedHosInformation(DetailedHosInformation detailedHosInformation){
        this.detailedHosInformation = detailedHosInformation;
        detailedHosInformation.setHospital(this);
    }

    //병원 수정
    public void modifyHospital(Hospital hospital) {
        this.licensingDate = hospital.getLicensingDate();
        this.hospitalName = hospital.getHospitalName();
        this.phoneNumber = hospital.getPhoneNumber();
        this.distinguishedName = hospital.getDistinguishedName();
        this.medicalSubjectInformation = hospital.getMedicalSubjectInformation();
        this.businessCondition = hospital.getBusinessCondition();
        this.cityName = hospital.getCityName();
    }

    /*생성자*/

    //병원 생성
    @Builder
    public Hospital(Long id, String licensingDate, String hospitalName,
                    String phoneNumber, String distinguishedName,
                    String medicalSubjectInformation, BusinessCondition businessCondition, String cityName) {
        this.id = id;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }

    //fk 제거를 위해서 staffHos 지우기
    public void deleteStaffHosId(){
        this.staffHosInformation = null;
    }

    //fk 제거를 위해서 DetailedHos 지우기
    public void deleteDetailedHosId(){
        this.detailedHosInformation = null;
    }


    public void updateHospital(String licensingDate, String hospitalName, String phoneNumber,
                               String distinguishedName, String medicalSubjectInformation,
                               BusinessCondition businessCondition, String cityName) {
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }
}