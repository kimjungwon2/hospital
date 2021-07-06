package site.hospital.repository.member.simplequery;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import site.hospital.domain.Authorization;

@Data
public class MemberSearchResult {
    private long memberId;

    private String memberIdName;
    private String password;
    private String nickName;
    private String userName;
    private int phoneNumber;

    //회원 권한 부여 상태. [NORMAL, STAFF, ADMIN]
    private Authorization authorizationStatus;

    //병원 번호
    private Long hospitalNumber;

    @QueryProjection
    public MemberSearchResult(long memberId, String memberIdName,
                              String password, String nickName,
                              String userName, int phoneNumber,
                              Authorization authorizationStatus, Long hospitalNumber) {
        this.memberId = memberId;
        this.memberIdName = memberIdName;
        this.password = password;
        this.nickName = nickName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.authorizationStatus = authorizationStatus;
        this.hospitalNumber = hospitalNumber;
    }
}
