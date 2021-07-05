package site.hospital.api.dto;

import lombok.Data;

@Data
public class CreateMemberResponse {
    long member_id;
    public CreateMemberResponse(long member_id){
        this.member_id = member_id;
    }
}
