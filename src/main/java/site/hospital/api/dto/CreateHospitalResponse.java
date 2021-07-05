package site.hospital.api.dto;

import lombok.Data;

@Data
public class CreateHospitalResponse {
    long id;
    public CreateHospitalResponse(long id){
        this.id = id;
    }
}
