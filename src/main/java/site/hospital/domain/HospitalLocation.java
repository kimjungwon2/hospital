package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalLocation {

    private double xCoordination;
    private double yCoordination;
    //위도, 경도
    private double latitude;
    private double longitude;
}
