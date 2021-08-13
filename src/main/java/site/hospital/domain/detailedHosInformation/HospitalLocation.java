package site.hospital.domain.detailedHosInformation;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalLocation {

    @Column(columnDefinition="Decimal(19,12)")
    private BigDecimal x_coordination;
    @Column(columnDefinition="Decimal(19,12)")
    private BigDecimal y_coordination;
    //위도, 경도
    @Column(columnDefinition="Decimal(19,12)")
    private BigDecimal latitude;
    @Column(columnDefinition="Decimal(19,12)")
    private BigDecimal longitude;

    @Builder
    public HospitalLocation(BigDecimal x_coordination, BigDecimal y_coordination,
                            BigDecimal latitude, BigDecimal longitude) {
        this.x_coordination = x_coordination;
        this.y_coordination = y_coordination;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
