package site.hospital.hospital.user.domain.detailedHosInformation;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalAddress {

    //지번주소
    @NotNull
    private String landLotBasedSystem;
    @NotNull
    private String roadBaseAddress;
    private String zipCode;

    @Builder
    public HospitalAddress(String landLotBasedSystem, String roadBaseAddress, String zipCode) {
        this.landLotBasedSystem = landLotBasedSystem;
        this.roadBaseAddress = roadBaseAddress;
        this.zipCode = zipCode;
    }
}
