package site.hospital.dto.staffHosInfo;

import lombok.Data;

@Data
public class AdminModifyStaffHosRequest{
    private Long hospitalId;
    private String photo;
    private String introduction;
    private String consultationHour;
    private String abnormality;
}