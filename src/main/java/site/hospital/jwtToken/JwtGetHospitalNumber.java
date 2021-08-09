package site.hospital.jwtToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class JwtGetHospitalNumber {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    public JwtGetHospitalNumber(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    //JWT TOKEN의 병원 번호를 꺼내오는 역할.
    public Long getJwtHospitalNumber(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            Long hospitalNumber = tokenProvider.getHospitalNumber(jwt);
            return hospitalNumber;
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
            return null;
        }
    }

    //Request Header에서 토큰 정보를 꺼내온다.
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
