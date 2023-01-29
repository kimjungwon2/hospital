package site.hospital.api.dto.hospital;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import site.hospital.domain.HospitalThumbnail;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class HospitalAdminViewThumbnailResponse {

    private final Long thumbnailId;
    private final String imageKey;

    public static HospitalAdminViewThumbnailResponse from(HospitalThumbnail hospitalThumbnail) {
        return HospitalAdminViewThumbnailResponse
                .builder()
                .thumbnailId(hospitalThumbnail.getId())
                .imageKey(hospitalThumbnail.getImageKey())
                .build();
    }

}
