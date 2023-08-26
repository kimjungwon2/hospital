package site.hospital.common.oauth;

import static site.hospital.common.oauth.OAuth2HttpRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;
import static site.hospital.common.oauth.OAuth2HttpRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class OAuth2RemoveCookie {

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }

}
