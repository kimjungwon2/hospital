package site.hospital.domain;

import lombok.AccessLevel;
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
}
