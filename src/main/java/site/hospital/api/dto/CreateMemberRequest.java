package site.hospital.api.dto;

import lombok.Data;
import site.hospital.domain.Authorization;


import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class CreateMemberRequest {

    //회원 이름
    private String userName;
    //회원 아이디
    private String memberIdName;
    private String password;
    private String nickName;
    private int phoneNumber;

}
