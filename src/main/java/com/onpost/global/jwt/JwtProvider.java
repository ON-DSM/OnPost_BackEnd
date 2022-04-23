package com.onpost.global.jwt;

import com.onpost.domain.entity.AuthDetails;
import com.onpost.domain.entity.member.RefreshToken;
import com.onpost.domain.repository.jpa.RefreshRepository;
import com.onpost.global.error.exception.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@Slf4j
public record JwtProvider(JwtProperties jwtProperties, CustomUserDetailsService userDetailsService,
                          RefreshRepository refreshRepository) {

    public String generateAccessToken(String authorities, String email) {
        return generateToken(authorities, email, jwtProperties.getAccessTokenExpiration(), "ACCESS");
    }

    public String generateRefreshToken(String authorities, String email) {
        String token = generateToken(authorities, email, jwtProperties.getRefreshTokenExpiration(), "REFRESH");
        refreshRepository.save(RefreshToken.builder()
                .Authority(authorities)
                .refreshToken(token)
                .email(email)
                .timeToLive(jwtProperties.getRefreshTokenExpiration()).build());
        return token;
    }

    public String generateToken(String authorities, String email, Long exp, String type) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .setHeaderParam(Header.TYPE, type)
                .setSubject(email)
                .claim(jwtProperties.getAUTHORIZATION_KEY(), authorities)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + exp))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(jwtProperties.getHeader());
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public Jws<Claims> getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다");
            throw WrongTokenException.EXCEPTION;
        } catch (SignatureException e) {
            log.info("특징이 다른 JWT 토큰입니다.");
            throw SignatureTokenException.EXCEPTION;
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw ExpiredAccessTokenException.EXCEPTION;
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
            throw UnsupportedTokenException.EXCEPTION;
        } catch (InvalidTokenException e) {
            log.info("유효하지 않는 JWT 토큰입니다.");
            throw InvalidTokenException.EXCEPTION;
        }
    }

    public Authentication getAuthentication(String jwt) {
        Claims claims = getClaims(jwt).getBody();

        AuthDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(principal, jwt, principal.getAuthorities());
    }
}
