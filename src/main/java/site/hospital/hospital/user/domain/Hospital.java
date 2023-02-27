package site.hospital.hospital.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.bookmark.user.domain.Bookmark;
import site.hospital.tag.manager.domain.PostTag;
import site.hospital.question.user.domain.Question;
import site.hospital.common.domain.BaseEntity;
import site.hospital.hospital.user.domain.detailedinfo.DetailedHosInformation;
import site.hospital.estimation.user.domain.Estimation;
import site.hospital.review.user.domain.reviewHospital.ReviewHospital;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    @OneToMany(mappedBy = "hospital")
    private List<Estimation> estimations = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<PostTag> postTags = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<Bookmark> bookmarks = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<ReviewHospital> reviewHospitals = new ArrayList<>();
    @OneToMany(mappedBy = "hospital")
    private List<HospitalImage> hospitalImages = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staffHosInformation_id")
    private StaffHosInformation staffHosInformation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "detailedHosInformation_id")
    private DetailedHosInformation detailedHosInformation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospitalThumbnail_id")
    private HospitalThumbnail hospitalThumbnail;

    @NotNull
    private String licensingDate;
    @NotNull
    private String hospitalName;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String distinguishedName;
    @NotNull
    private String medicalSubjectInformation;
    @NotNull
    private String cityName;
    @Enumerated(EnumType.STRING)
    @NotNull
    private BusinessCondition businessCondition;


    //== 연관 관계 메서드 ==//
    public void changeStaffHosInformation(StaffHosInformation staffHosInformation) {
        this.staffHosInformation = staffHosInformation;
        staffHosInformation.setHospital(this);
    }

    public void changeDetailedHosInformation(DetailedHosInformation detailedHosInformation) {
        this.detailedHosInformation = detailedHosInformation;
        detailedHosInformation.setHospital(this);
    }

    public void changeHospitalThumbnail(HospitalThumbnail hospitalThumbnail) {
        this.hospitalThumbnail = hospitalThumbnail;
        hospitalThumbnail.setHospital(this);
    }


    /*생성자*/

    @Builder
    public Hospital(Long id,
            String licensingDate,
            String hospitalName,
            String phoneNumber,
            String distinguishedName,
            String medicalSubjectInformation,
            BusinessCondition businessCondition,
            String cityName
    ) {
        this.id = id;
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }

    public void deleteHospitalAdditionalInfo() {
        this.staffHosInformation = null;
    }
    public void deleteDetailedHosId() {
        this.detailedHosInformation = null;
    }
    public void deleteHospitalThumbnailId() {
        this.hospitalThumbnail = null;
    }

    public void modifyHospital(Hospital hospital) {
        this.licensingDate = hospital.getLicensingDate();
        this.hospitalName = hospital.getHospitalName();
        this.phoneNumber = hospital.getPhoneNumber();
        this.distinguishedName = hospital.getDistinguishedName();
        this.medicalSubjectInformation = hospital.getMedicalSubjectInformation();
        this.businessCondition = hospital.getBusinessCondition();
        this.cityName = hospital.getCityName();
    }

    public void updateHospital(
            String licensingDate,
            String hospitalName,
            String phoneNumber,
            String distinguishedName,
            String medicalSubjectInformation,
            BusinessCondition businessCondition,
            String cityName
    ) {
        this.licensingDate = licensingDate;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.distinguishedName = distinguishedName;
        this.medicalSubjectInformation = medicalSubjectInformation;
        this.businessCondition = businessCondition;
        this.cityName = cityName;
    }
}
