package site.hospital.common.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.common.jwt.CustomUserDetail;
import site.hospital.member.user.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) {
        Optional<Member> memberOptional = memberRepository.findOneEmailByMemberIdName(name);

        Member member = memberOptional
                .orElseThrow(() -> new IllegalStateException("로그인하려는 아이디가 존재하지 않습니다."));

        List<MemberAuthority> memberAuthorities = memberRepository.findMemberAuthorities(name);

        List<GrantedAuthority> grantedAuthorities = memberAuthorities.stream()
                .map(a -> new SimpleGrantedAuthority(
                        a.getAuthority().getAuthorizationStatus().toString()))
                .collect(Collectors.toList());

        return new CustomUserDetail(member.getMemberIdName()
                , member.getPassword(), grantedAuthorities,
                member.getPhoneNumber(), member.getHospitalNumber(),
                member.getId(), member.getNickName(), member.getMemberStatus());
    }
}
