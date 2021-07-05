package site.hospital.domain;

import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public HospitalLocation(double xCoordination, double yCoordination, double latitude, double longitude) {
        this.xCoordination = xCoordination;
        this.yCoordination = yCoordination;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
