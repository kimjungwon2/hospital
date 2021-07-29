package site.hospital.domain.detailedHosInformation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.hospital.domain.baseEntity.BaseEntity;
import site.hospital.domain.Hospital;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailedHosInformation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailedHosInformation_id")
    private Long id;

    @OneToOne(mappedBy = "detailedHosInformation", fetch = FetchType.LAZY)
    private Hospital hospital;

    private int numberHealthcareProvider;
    private int numberWard;
    //입원실 수
    private int numberPatientRoom;

    @Embedded
    private HospitalAddress hospitalAddress;

    //병원 장소(좌표)
    @Embedded
    private HospitalLocation hospitalLocation;


    public void modifyDetailedHosInformation(DetailedHosInformation detailedHosInformation){
        this.numberHealthcareProvider = detailedHosInformation.getNumberHealthcareProvider();
        this.numberWard = detailedHosInformation.getNumberWard();
        this.numberPatientRoom = detailedHosInformation.getNumberPatientRoom();
        this.hospitalAddress = detailedHosInformation.getHospitalAddress();
        this.hospitalLocation = detailedHosInformation.getHospitalLocation();
    }

    @Builder
    public DetailedHosInformation(int numberHealthcareProvider, int numberWard, int numberPatientRoom,
                                  HospitalAddress hospitalAddress, HospitalLocation hospitalLocation) {
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
                .x_coordination(hospitalLocation.getX_coordination())
                .y_coordination(hospitalLocation.getY_coordination())
                .build();
    }

    //연관관계 때문에 set 설정
    public void setHospital(Hospital hospital){
        this.hospital = hospital;
    }


    //비즈니스 메서드
    public void updateDetailedHosInformation(int numberWard, int numberHealthcareProvider, int numberPatientRoom,
                                             HospitalAddress hospitalAddress, HospitalLocation hospitalLocation){
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
                .x_coordination(hospitalLocation.getX_coordination())
                .y_coordination(hospitalLocation.getY_coordination())
                .build();
    }

}
