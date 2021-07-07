package site.hospital.repository.member.simplequery;

import lombok.Data;
import site.hospital.domain.Authorization;

@Data
public class MemberSearchCondition {
    //모두 검색
    private String allSearch;

    private Long memberId;
    private String memberIdName;
    private String nickName;
    private String userName;
    private String phoneNumber;
    private Authorization authorizationStatus;
    private Long hospitalNumber;

}
