package site.hospital.doctor.api.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(access = AccessLevel.PRIVATE)
public class DoctorCreateResponse {

    private final Long doctorId;

    public static DoctorCreateResponse from(Long doctorId) {
        return DoctorCreateResponse
                .builder()
                .doctorId(doctorId)
                .build();
    }
}
