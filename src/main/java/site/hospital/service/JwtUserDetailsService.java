package site.hospital.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.domain.member.Member;
import site.hospital.domain.member.MemberAuthority;
import site.hospital.domain.member.MemberStatus;
import site.hospital.repository.member.MemberRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name){
        Optional<Member> memberOptional = memberRepository.findOneEmailByMemberIdName(name);

        Member member = memberOptional.orElseThrow(()->new IllegalStateException("로그인하려는 아이디가 존재하지 않습니다."));

        List<MemberAuthority> memberAuthorities = memberRepository.memberAuthorities(name);

        List<GrantedAuthority> grantedAuthorities = memberAuthorities.stream()
                .map(a-> new SimpleGrantedAuthority(a.getAuthority().getAuthorizationStatus().toString())).collect(Collectors.toList());

        return new CustomUserDetail(member.getMemberIdName()
                ,member.getPassword(),grantedAuthorities,
                member.getPhoneNumber(),member.getHospitalNumber(),
                member.getId(),member.getNickName(),member.getMemberStatus());
    }

    public class CustomUserDetail extends User {

        String phoneNumber;
        Long hospitalNumber;
        Long memberId;
        String nickName;
        MemberStatus memberStatus;

        public CustomUserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities,
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

}
