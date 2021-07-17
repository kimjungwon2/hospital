package site.hospital.repository.hospital.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewHospitalDto {
    private Long hospitalId;
    private Integer sumPrice;

    @QueryProjection
    public ReviewHospitalDto(Long hospitalId, Integer sumPrice) {
        this.hospitalId = hospitalId;
        this.sumPrice = sumPrice;
    }
}
