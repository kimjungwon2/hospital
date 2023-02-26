package site.hospital.doctor.admin.api.dto;

import lombok.Data;

@Data
public class DoctorAdminCreateRequest {

    private Long hospitalAdditionalInfoId;
    private String name;
    private String history;
}
