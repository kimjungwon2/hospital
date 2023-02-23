package site.hospital.common.service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import site.hospital.member.user.domain.Authorization;
import site.hospital.member.user.domain.MemberAuthority;
import site.hospital.common.jwtToken.JwtFilter;
import site.hospital.common.jwtToken.TokenProvider;
import site.hospital.member.user.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ManagerJwtAccessService {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    //staff 권한 접근.
    public void managerAccess(ServletRequest servletRequest, Long memberId,
            Long existingHospitalId) {

        Long JwtHospitalId = getJwtHospitalNumber(servletRequest);

        //멤버 권한의 병원 번호
        MemberAuthority findMemberManager = memberRepository
                .findMemberStaffAuthority(memberId, Authorization.ROLE_MANAGER);

        if (findMemberManager == null) {
            throw new AccessDeniedException("해당 멤버는 Manager 권한이 없습니다.");
        } else if (findMemberManager.getHospitalNo() == 0) {
            throw new AccessDeniedException("관리자 계정은 관리자 기능을 이용해주세요.");
        }
        //토큰 번호와 권한의 병원 정보가 같지 않으면 인증 오류
        else if (JwtHospitalId != findMemberManager.getHospitalNo()) {
            throw new AccessDeniedException("토큰 번호와 권한 번호가 일치하지 않습니다.");
        }
        //권한의 병원 번호와 실제 병원 번호가 다르면 접근 차단.
        else if (findMemberManager.getHospitalNo() != existingHospitalId) {
            throw new AccessDeniedException("자신의 병원 번호만 조작이 가능합니다.");
        }
    }

    //token 병원 정보 얻기
    public Long getHospitalNumber(ServletRequest servletRequest) {
        //토큰의 병원 번호
        Long JwtHospitalId = getJwtHospitalNumber(servletRequest);

        return JwtHospitalId;
    }

    //JWT TOKEN의 병원 번호를 꺼내오는 역할.
    private Long getJwtHospitalNumber(ServletRequest servletRequest) {
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
