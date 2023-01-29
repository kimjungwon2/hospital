package site.hospital.api.dto.hospital;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class HospitalResponse {

    private final Long id;

    public static HospitalResponse from(Long id) {
        return HospitalResponse
                .builder()
                .id(id)
                .build();
    }

}
