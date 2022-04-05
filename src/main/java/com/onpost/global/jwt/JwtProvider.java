package com.onpost.global.jwt;

import com.onpost.domain.dto.auth.TokenDto;
import com.onpost.global.error.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtProvider implements InitializingBean {

    private final String secretKey;

    public static final String header = "Authentication";

    private final String AUTHORIZATION_KEY;

    private final long accessTokenExpiration;

    private final long refreshTokenExpiration;

    public final AuthenticationManagerBuilder authenticationManagerBuilder;

    private Key key;

    public JwtProvider(
            @Value("${jwt.key}") String AUTHORIZATION_KEY,
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access}") long accessTokenValidityInseconds,
            @Value("${jwt.refresh}") long refreshTokenValidityInseconds,
            AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.secretKey = secret;
        this.accessTokenExpiration = accessTokenValidityInseconds * 1000;
        this.refreshTokenExpiration = refreshTokenValidityInseconds * 1000;
        this.AUTHORIZATION_KEY = AUTHORIZATION_KEY;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(String email, String authorities) {
        Date now = new Date();
        return new TokenDto(
                generateAccessToken(authorities, now, email),
                generateRefreshToken(authorities, now, email));
    }

    public String generateAccessToken(String authorities, Date now, String email) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.accessTokenExpiration))
                .compact();
    }

    public String generateRefreshToken(String authorities, Date now, String email) {
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + this.refreshTokenExpiration))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(header);
        if(StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        } catch (InvalidTokenException e) {
            log.info("유효하지 않는 JWT 토큰입니다.");
        }
        return false;
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = getClaims(jwt);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORIZATION_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, jwt, authorities);
    }

    public String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


}
