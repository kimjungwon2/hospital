package site.hospital.common.oauth;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.hospital.member.user.domain.Authority;
import site.hospital.member.user.domain.Authorization;
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.member.user.repository.AuthorityRepository;
import site.hospital.member.user.repository.MemberAuthorityRepository;
import site.hospital.member.user.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);
        List<MemberAuthority> memberAuthorities = memberRepository.findMemberAuthorities(member.getMemberIdName());
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(memberAuthorities);

        return new DefaultOAuth2User(
                grantedAuthorities,
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Optional<Member> member = memberRepository.findOneEmailByMemberIdName(attributes.getEmail());

        if (member.isPresent()) {
            return updateMember(attributes, member);
        }

        return saveMember(attributes);
    }

    private Member updateMember(OAuthAttributes attributes, Optional<Member> member) {
        log.info("기존 {} 회원 수정",attributes.getName());

        member.map(entity -> entity.updateOauth(attributes.getName(), attributes.getPhoneNumber()));

        return member.orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }

    private Member saveMember(OAuthAttributes attributes) {
        log.info("새로운 {} 회원 생성",attributes.getName());

        Member newMember = attributes.toEntity();
        Authority userAuthority = findUserAuthority();
        saveMemberWithAuthority(newMember, userAuthority);

        return newMember;
    }

    private Authority findUserAuthority() {
        Authority authority = authorityRepository.findByAuthorizationStatus(Authorization.ROLE_USER);

        if (authority == null) {
            throw new IllegalStateException("USER 권한 데이터가 없습니다.");
        }
        return authority;
    }

    private void saveMemberWithAuthority(Member createdMember, Authority authority) {
        memberRepository.save(createdMember);

        MemberAuthority memberAuthority = MemberAuthority
                .builder()
                .member(createdMember)
                .authority(authority)
                .build();

        memberAuthorityRepository.save(memberAuthority);
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<MemberAuthority> memberAuthorities) {

        return memberAuthorities
                .stream()
                .map(a -> new SimpleGrantedAuthority(
                        a.getAuthority().getAuthorizationStatus().toString()))
                .collect(Collectors.toList());

    }
}
