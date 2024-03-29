package site.hospital.hospital.user.api.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.hospital.user.domain.HospitalImage;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class HospitalViewImageResponse {

    private final Long hospitalImageId;
    private final String imageKey;

    public static HospitalViewImageResponse from(HospitalImage hospitalImage) {
        return HospitalViewImageResponse
                .builder()
                .hospitalImageId(hospitalImage.getId())
                .imageKey(hospitalImage.getImageKey())
                .build();
    }

}
