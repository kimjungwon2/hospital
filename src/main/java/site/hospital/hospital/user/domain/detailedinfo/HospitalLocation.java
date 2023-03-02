package site.hospital.hospital.user.domain.detailedinfo;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalLocation {

    @Column(columnDefinition = "Decimal(19,12)")
    private BigDecimal xCoordination;
    @Column(columnDefinition = "Decimal(19,12)")
    private BigDecimal yCoordination;

    //위도, 경도
    @Column(columnDefinition = "Decimal(19,12)")
    @NotNull
    private BigDecimal latitude;
    @Column(columnDefinition = "Decimal(19,12)")
    @NotNull
    private BigDecimal longitude;

    @Builder
    public HospitalLocation(
            BigDecimal xCoordination,
            BigDecimal yCoordination,
            BigDecimal latitude,
            BigDecimal longitude
    ) {
        this.xCoordination = xCoordination;
        this.yCoordination = yCoordination;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
