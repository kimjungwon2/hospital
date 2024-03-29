package site.hospital.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@PropertySource("classpath:application-jwt.yml")
public class TokenProvider implements Serializable {

    private static final long serialVersionUID = -798416586417070603L;
    private static final long JWT_TOKEN_VALIDITY = (long)30 * 60 * 1000; //30분

    @Value("${jwt.secret}")
    private String secret;

    private static final String AUTHORITIES_KEY = "auth";
    private static final String PHONE_KEY = "phoneNumber";
    private static final String HOSPITAL_NUMBER_KEY = "hospitalNumber";


    public String createToken(Authentication authentication, String phoneNumber) {

        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = createValidity();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(PHONE_KEY, phoneNumber)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .compact();
    }

    public String createToken(Authentication authentication) {

        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = createValidity();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .compact();
    }

    public String createManagerToken(
            Authentication authentication,
            String phoneNumber,
            Long hospitalNumber
    ) {
        String authorities = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date validity = createValidity();

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(PHONE_KEY, phoneNumber)
                .claim(HOSPITAL_NUMBER_KEY, hospitalNumber)
                .signWith(SignatureAlgorithm.HS512, secret)
                .setExpiration(validity)
                .compact();
    }

    public Long getHospitalNumberInManager(String token) {
        Claims claims = createClaims(token);

        checkHospitalNumberNull(claims);

        String hospitalNumber = claims.get(HOSPITAL_NUMBER_KEY).toString();

        return Long.parseLong(hospitalNumber);
    }

    public Authentication getAuthentication(String token) {

        Claims claims = createClaims(token);

        Collection<? extends GrantedAuthority> authorities = getAuthoritiesByClaims(claims);
        User principal = createUserByAuthorities(claims, authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private User createUserByAuthorities(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        return new User(claims.getSubject(), "", authorities);
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesByClaims(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY)
                       .toString()
                       .split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }

    private Claims createClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private Date createValidity() {
        long now = (new Date()).getTime();

        return new Date(now + JWT_TOKEN_VALIDITY);
    }

    private void checkHospitalNumberNull(Claims claims) {
        if (claims.get(HOSPITAL_NUMBER_KEY) == null) {
            throw new AccessDeniedException("병원 번호가 존재하지 않는 계정");
        }
    }
}