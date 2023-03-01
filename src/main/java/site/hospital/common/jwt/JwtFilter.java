package site.hospital.common.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class JwtFilter extends GenericFilterBean {

    private static final Logger log = LoggerFactory.getLogger(GenericFilterBean.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    //JWT TOKEN의 인증정보를 Security Context에 저장하는 역할 수행.
    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = takeToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (checkTokenValue(jwt) && tokenProvider.validateToken(jwt)) {
            injectAuthenticationInSecurityContextHolder(jwt);
        } else {
            log.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void injectAuthenticationInSecurityContextHolder(String jwt) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String takeToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (checkBearerPrefix(bearerToken)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean checkTokenValue(String jwt) {
        return StringUtils.hasText(jwt);
    }

    private boolean checkBearerPrefix(String bearerToken) {
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ");
    }
}