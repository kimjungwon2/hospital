package site.hospital.dto;

import lombok.Builder;
import lombok.Data;
import site.hospital.domain.member.Authorization;

@Data
public class AdminMemberSearchCondition {
    //모두 검색
    private String allSearch;

    private Long memberId;
    private String memberIdName;
    private String nickName;
    private String userName;
    private String phoneNumber;
    private Authorization authorizationStatus;
    private Long hospitalNumber;

    @Builder
    public AdminMemberSearchCondition(String allSearch, Long memberId, String memberIdName,
                                      String nickName, String userName,
                                      String phoneNumber, Authorization authorizationStatus,
                                      Long hospitalNumber) {
        this.allSearch = allSearch;
        this.memberId = memberId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.authorizationStatus = authorizationStatus;
        this.hospitalNumber = hospitalNumber;
    }
}
