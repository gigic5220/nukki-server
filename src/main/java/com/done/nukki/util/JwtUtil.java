package com.done.nukki.util;

import com.done.nukki.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간
    private static final long REFRESH_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    public String generateToken(boolean isAccessToken, String socialAccount, String provider, String status, String role) {
        return Jwts.builder()
            .setSubject(socialAccount)
            .claim("provider", provider)
            .claim("status", status)
            .claim("roles", role)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + (isAccessToken ? EXPIRATION_TIME : REFRESH_EXPIRATION_TIME)))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public long getExpirationInMillis(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .getTime();
    }


    public String extractSocialAccount(String token) {
        return parseToken(token).getSubject();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료되었습니다.", e);
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("토큰이 잘못되었습니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("지원되지 않는 토큰 형식입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("JWT 토큰이 비어 있습니다.", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (InvalidTokenException e) {
            throw e;
        }
    }

}