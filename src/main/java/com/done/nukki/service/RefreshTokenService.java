package com.done.nukki.service;

import com.done.nukki.entity.RefreshToken;
import com.done.nukki.entity.Member;
import com.done.nukki.repository.RefreshTokenRepository;
import com.done.nukki.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    public void save(int memberId, String refreshTokenValue) {

        Member member = memberService.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        // 기존 RefreshToken 조회
        RefreshToken existingToken = refreshTokenRepository.findByMemberId(memberId).orElse(null);

        if (existingToken != null) {
            // 기존 토큰 업데이트
            existingToken.setToken(refreshTokenValue);
            existingToken.setUpdated(LocalDateTime.now());
            refreshTokenRepository.save(existingToken);
        } else {
            // 새로운 토큰 삽입
            RefreshToken newToken = new RefreshToken(refreshTokenValue, member);
            refreshTokenRepository.save(newToken);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public Claims validateRefreshToken(String token) {
        try {
            // 1. 데이터베이스에서 RefreshToken 조회
            RefreshToken storedToken = findByToken(token).orElseThrow(() -> new RuntimeException("Invalid or expired refresh token"));

            // 2. RefreshToken과 연결된 사용자 조회
            Member member = memberService.findById(storedToken.getMember().getId()).orElseThrow(() -> new RuntimeException("Member not found"));

            // 3. 클라이언트가 제공한 토큰과 데이터베이스의 토큰 일치 여부 확인
            if (!storedToken.getToken().equals(token)) {
                throw new RuntimeException("Token mismatch between database and provided token");
            }

            // 4. JWT 파싱 - 만료 시간 검증
            Claims claims = jwtUtil.parseToken(token);

            // 5. 토큰의 subject(socialAccount)가 데이터베이스의 사용자와 일치하는지 검증
            if (!claims.getSubject().equals(member.getSocialAccount())) {
                throw new JwtException("Token does not belong to the authenticated Member");
            }

            return claims;

        } catch (ExpiredJwtException e) {
            System.out.println("Expired token: e = " + e);
            throw e; // ExpiredJwtException 그대로 던짐
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid token: e = " + e);
            throw e; // JwtException 그대로 던짐
        }
    }

    /*public String refreshAccessToken(String refreshToken) {
        validateRefreshToken(refreshToken);
    }*/
}