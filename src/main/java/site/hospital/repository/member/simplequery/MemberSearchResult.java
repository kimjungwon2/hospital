package site.hospital.repository.member.simplequery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.member.Authorization;

@Data
public class MemberSearchResult {
    private long memberId;
    private String memberIdName;
    private String nickName;
    private String userName;
    private String phoneNumber;

    //회원 권한 부여 상태. [NORMAL, STAFF, ADMIN]
    private Authorization authorizationStatus;

    //병원 번호
    private Long hospitalNumber;

    @QueryProjection
    public MemberSearchResult(long memberId, String memberIdName,
                              String nickName,
                              String userName, String phoneNumber,
                              Authorization authorizationStatus, Long hospitalNumber) {
        this.memberId = memberId;
        this.memberIdName = memberIdName;
        this.nickName = nickName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.authorizationStatus = authorizationStatus;
        this.hospitalNumber = hospitalNumber;
    }
}
