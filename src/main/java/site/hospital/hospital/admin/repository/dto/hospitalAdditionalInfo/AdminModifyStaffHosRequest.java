package site.hospital.hospital.admin.repository.dto.hospitalAdditionalInfo;

import lombok.Data;

@Data
public class AdminModifyStaffHosRequest{
    private String introduction;
    private String consultationHour;
    private String abnormality;
}