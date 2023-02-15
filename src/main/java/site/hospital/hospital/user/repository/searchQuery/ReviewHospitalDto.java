package site.hospital.hospital.user.repository.searchQuery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ReviewHospitalDto {

    private Long hospitalId;
    private Double averageRate;
    private Long reviewCount;

    @QueryProjection
    public ReviewHospitalDto(Long hospitalId, Double averageRate, Long reviewCount) {
        this.hospitalId = hospitalId;
        this.averageRate = averageRate;
        this.reviewCount = reviewCount;
    }
}
