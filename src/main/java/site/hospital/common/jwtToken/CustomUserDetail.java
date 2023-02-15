package site.hospital.common.jwtToken;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import site.hospital.member.user.domain.MemberStatus;

public class CustomUserDetail extends User {

    String phoneNumber;
    Long hospitalNumber;
    Long memberId;
    String nickName;
    MemberStatus memberStatus;

    public CustomUserDetail(String username, String password,
            Collection<? extends GrantedAuthority> authorities,
            String phoneNumber, Long hospitalNumber,
            Long memberId, String nickName, MemberStatus memberStatus) {
        super(username, password, authorities);
        this.phoneNumber = phoneNumber;
        this.hospitalNumber = hospitalNumber;
        this.memberId = memberId;
        this.nickName = nickName;
        this.memberStatus = memberStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getHospitalNumber() {
        return hospitalNumber;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNickName() {
        return nickName;
    }

    public MemberStatus getMemberStatus() {
        return memberStatus;
    }
}