package site.hospital.common.oauth;

import static site.hospital.common.oauth.OAuth2HttpRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import java.io.IOException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import site.hospital.common.jwt.TokenProvider;
import java.util.*;
import site.hospital.member.user.domain.MemberStatus;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final OAuth2RemoveAuthenticationCookie oAuth2RemoveAuthenticationCookie;

    public static final String LOGIN_COOKIE_KEY_MEMBER_ID = "member_id";
    public static final String LOGIN_COOKIE_KEY_TOKEN = "token";
    public static final String LOGIN_COOKIE_KEY_NICKNAME = "nick_name";
    public static final String LOGIN_COOKIE_KEY_MEMBER_STATUS = "member_status";
    private final int loginCookieExpireSeconds = 6 * 30 * 24 * 60 * 60; // 6달

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        setLoginCookies(response, authentication);

        return UriComponentsBuilder
                .fromUriString(targetUrl)
                .queryParam("success", true)
                .build()
                .toUriString();
    }

    private void setLoginCookies(HttpServletResponse response, Authentication authentication) {
        String token = tokenProvider.createToken(authentication);

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        setMemberStatusCookie(response, oAuth2User);
        setNickNameCookie(response, attributes);
        setTokenCookie(response, token);
        setMemberIdCookie(response, attributes);
    }


    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        oAuth2RemoveAuthenticationCookie.removeAuthorizationRequestCookies(request, response);
    }

    private void setMemberIdCookie(HttpServletResponse response, Map<String, Object> attributes) {
        if(attributes.get("memberId")==null){
            throw new IllegalStateException("회원이 존재하지 않습니다.");
        } else if(attributes.get("memberId")!=null){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_MEMBER_ID, String.valueOf(attributes.get("memberId")), loginCookieExpireSeconds);
        }
    }

    private void setTokenCookie(HttpServletResponse response, String token) {
        if(token ==null){
            throw new IllegalStateException("로그인 실패");
        } else if(token!=null){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_TOKEN, token, loginCookieExpireSeconds);
        }
    }


    private void setMemberStatusCookie(HttpServletResponse response, OAuth2User oAuth2User) {
        if(oAuth2User.getAuthorities().size()==1){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_MEMBER_STATUS, String.valueOf(MemberStatus.NORMAL), loginCookieExpireSeconds);
        } else if(oAuth2User.getAuthorities().size()==2){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_MEMBER_STATUS, String.valueOf(MemberStatus.STAFF), loginCookieExpireSeconds);
        } else if(oAuth2User.getAuthorities().size()==3){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_MEMBER_STATUS, String.valueOf(MemberStatus.ADMIN), loginCookieExpireSeconds);
        } else{
            throw new IllegalStateException("권한이 잘못 부여됐습니다.");
        }
    }

    private void setNickNameCookie(HttpServletResponse response, Map<String, Object> attributes) {
        if(attributes.get("nickname")==null){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_NICKNAME, (String) attributes.get("name"),loginCookieExpireSeconds);
        } else if(attributes.get("nickname")!=null){
            CookieUtils.setCookie(response, LOGIN_COOKIE_KEY_NICKNAME, (String) attributes.get("nickname"), loginCookieExpireSeconds);
        }
    }

}