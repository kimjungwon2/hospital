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
import site.hospital.member.user.domain.Member;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.member.user.repository.MemberRepository;
import site.hospital.member.user.service.MemberService;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);

        List<GrantedAuthority> grantedAuthorities = memberConvertToGrantedAuthorities(member);

        return new DefaultOAuth2User(
                grantedAuthorities,
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private List<GrantedAuthority> memberConvertToGrantedAuthorities(Member member) {
        List<MemberAuthority> memberAuthorities = memberRepository.findMemberAuthorities(member.getMemberIdName());
        List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(memberAuthorities);
        return grantedAuthorities;
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {
        Optional<Member> member = memberRepository.findOneEmailByMemberIdName(attributes.getEmail());

        if (member.isPresent()) {
            convertToMutableHashTable(attributes, member.get());

            return updateMember(attributes, member);
        }

        return saveMember(attributes);
    }

    private void convertToMutableHashTable(OAuthAttributes attributes, Member member) {
        Map<String, Object> inputData = new LinkedHashMap<>();
        inputData.putAll(attributes.getAttributes());
        inputData.put("memberId", member.getId());
        attributes.setAttributes(inputData);
    }

    private Member updateMember(OAuthAttributes attributes, Optional<Member> member) {
        log.info("기존 {} 회원 수정",attributes.getName());

        member.map(entity -> entity.updateOauth(attributes.getName(), attributes.getPhoneNumber()));

        return member.orElseThrow(() -> new IllegalArgumentException("멤버가 존재하지 않습니다."));
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<MemberAuthority> memberAuthorities) {

        return memberAuthorities
                .stream()
                .map(a -> new SimpleGrantedAuthority(
                        a.getAuthority().getAuthorizationStatus().toString()))
                .collect(Collectors.toList());

    }

    private Member saveMember(OAuthAttributes attributes) {
        log.info("새로운 {} 회원 생성",attributes.getName());

        Member newMember = attributes.toEntity();
        Authority userAuthority = memberService.findUserAuthority();
        memberService.saveMemberWithAuthority(newMember, userAuthority);

        convertToMutableHashTable(attributes, newMember);

        return newMember;
    }
}
