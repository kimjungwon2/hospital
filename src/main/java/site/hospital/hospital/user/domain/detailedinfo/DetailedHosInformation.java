package site.hospital.hospital.user.domain.detailedinfo;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.common.domain.BaseEntity;
import site.hospital.hospital.user.domain.Hospital;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailedHosInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailedHosInformation_id")
    private Long id;

    @OneToOne(mappedBy = "detailedHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    @NotNull
    private int numberHealthcareProvider;
    @NotNull
    private int numberWard;
    @NotNull
    private int numberPatientRoom;

    @Embedded
    @NotNull
    private HospitalAddress hospitalAddress;

    @Embedded
    private HospitalLocation hospitalLocation;


    public void modifyDetailedHosInformation(DetailedHosInformation detailedHosInformation) {
        this.numberHealthcareProvider = detailedHosInformation.getNumberHealthcareProvider();
        this.numberWard = detailedHosInformation.getNumberWard();
        this.numberPatientRoom = detailedHosInformation.getNumberPatientRoom();
        this.hospitalAddress = detailedHosInformation.getHospitalAddress();
        this.hospitalLocation = detailedHosInformation.getHospitalLocation();
    }

    @Builder
    public DetailedHosInformation(
            int numberHealthcareProvider,
            int numberWard,
            int numberPatientRoom,
            HospitalAddress hospitalAddress,
            HospitalLocation hospitalLocation
    ) {
        this.numberHealthcareProvider = numberHealthcareProvider;
        this.numberWard = numberWard;
        this.numberPatientRoom = numberPatientRoom;

        this.hospitalAddress = hospitalAddress
                .builder()
                .roadBaseAddress(hospitalAddress.getRoadBaseAddress())
                .landLotBasedSystem(hospitalAddress.getLandLotBasedSystem())
                .zipCode(hospitalAddress.getZipCode())
                .build();

        this.hospitalLocation = hospitalLocation
                .builder()
                .latitude(hospitalLocation.getLatitude())
                .longitude(hospitalLocation.getLongitude())
                .x_coordination(hospitalLocation.getX_coordination())
                .y_coordination(hospitalLocation.getY_coordination())
                .build();
    }

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public void updateDetailedHosInformation(
            Integer numberWard,
            Integer numberHealthcareProvider,
            Integer numberPatientRoom,
            HospitalAddress hospitalAddress,
            HospitalLocation hospitalLocation
    ) {
        this.numberHealthcareProvider = numberHealthcareProvider;
        this.numberWard = numberWard;
        this.numberPatientRoom = numberPatientRoom;

        this.hospitalAddress = hospitalAddress
                .builder()
                .roadBaseAddress(hospitalAddress.getRoadBaseAddress())
                .landLotBasedSystem(hospitalAddress.getLandLotBasedSystem())
                .zipCode(hospitalAddress.getZipCode())
                .build();

        this.hospitalLocation = hospitalLocation
                .builder()
                .latitude(hospitalLocation.getLatitude())
                .longitude(hospitalLocation.getLongitude())
                .x_coordination(hospitalLocation.getX_coordination())
                .y_coordination(hospitalLocation.getY_coordination())
                .build();
    }

}
