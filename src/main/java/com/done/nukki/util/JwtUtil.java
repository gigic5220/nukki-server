package com.done.nukki.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

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

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료됨: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("토큰이 잘못됨: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("지원되지 않는 토큰 형식: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT 토큰이 비어 있음: " + e.getMessage());
        }
        return false;
    }

    public String extractSocialAccount(String token) {
        return parseToken(token).getSubject();
    }
}