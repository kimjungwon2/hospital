package site.hospital.domain.detailedHosInformation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalAddress  {

    //지번주소
    private String landLotBasedSystem;
    private String roadBaseAddress;
    private String zipCode;

    @Builder
    public HospitalAddress(String landLotBasedSystem, String roadBaseAddress, String zipCode) {
        this.landLotBasedSystem = landLotBasedSystem;
        this.roadBaseAddress = roadBaseAddress;
        this.zipCode = zipCode;
    }
}
