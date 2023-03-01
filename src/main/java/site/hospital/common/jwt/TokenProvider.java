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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@PropertySource("classpath:application-jwt.yml")
public class TokenProvider implements Serializable {

    private static final long serialVersionUID = -798416586417070603L;
    private static final long JWT_TOKEN_VALIDITY = (long)60 * 60 * 3000;

    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

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
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private User createUserByAuthorities(Claims claims, Collection<? extends GrantedAuthority> authorities) {
        User principal = new User(claims.getSubject(), "", authorities);
        return principal;
    }

    private Collection<? extends GrantedAuthority> getAuthoritiesByClaims(Claims claims) {
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY)
                       .toString()
                       .split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return authorities;
    }

    private Claims createClaims(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    private Date createValidity() {
        long now = (new Date()).getTime();

        Date validity = new Date(now + JWT_TOKEN_VALIDITY);

        return validity;
    }

    private void checkHospitalNumberNull(Claims claims) {
        if (claims.get(HOSPITAL_NUMBER_KEY) == null) {
            throw new AccessDeniedException("병원 번호가 존재하지 않는 계정");
        }
    }
}