package site.hospital.api.dto;

import lombok.Data;

@Data
public class CreateMemberResponse {
    long id;

    public CreateMemberResponse(long id){
        this.id = id;
    }
}
