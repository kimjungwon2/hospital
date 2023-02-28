package site.hospital.member.admin.repository.dto;

import lombok.Builder;
import lombok.Data;
import site.hospital.member.user.domain.MemberStatus;

@Data
public class AdminMemberSearchCondition {

    //모두 검색
    private String allSearch;

    private Long memberId;
    private String memberIdName;
    private String nickName;
    private String userName;
    private String phoneNumber;
    private MemberStatus memberStatus;
    private Long hospitalNumber;

    @Builder
    public AdminMemberSearchCondition(
            String allSearch,
            Long memberId,
            String memberIdName,
            String nickName,
            String userName,
            String phoneNumber,
            MemberStatus memberStatus,
            Long hospitalNumber
    ) {
        this.allSearch = allSearch;
        this.memberId = memberId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.memberStatus = memberStatus;
        this.hospitalNumber = hospitalNumber;
    }
}
