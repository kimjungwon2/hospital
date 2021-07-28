package site.hospital.jwtToken;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements Serializable {

    private static final long serialVersionUID = -798416586417070603L;
    private static final long JWT_TOKEN_VALIDITY = 30 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;


    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";
    private static final String NICKNAME_KEY = "nickName";
    private static final String PHONE_KEY = "phoneNumber";


    //토큰 생성
    public String createToken(Authentication authentication, String phoneNumber) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity = new Date(now + this.JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .claim(PHONE_KEY, phoneNumber)
                .signWith(SignatureAlgorithm.HS512,secret)
                .setExpiration(validity)
                .compact();
    }

    //Authentication 반환.
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //권한 정보를 이용해서 유저 객체 생성
        User principal = new User(claims.getSubject(), "", authorities);

        //이를 이용해 유저 객체를 만들어 최종적으로 Authentication 객체를 리턴한다.
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    //토큰의 유효성 검사
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
}