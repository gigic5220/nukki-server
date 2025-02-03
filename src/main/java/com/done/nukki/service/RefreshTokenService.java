package com.done.nukki.service;

import com.done.nukki.dto.res.TokenRefreshResDto;
import com.done.nukki.entity.RefreshToken;
import com.done.nukki.entity.Member;
import com.done.nukki.exception.InvalidTokenException;
import com.done.nukki.repository.RefreshTokenRepository;
import com.done.nukki.util.JwtUtil;
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

    public TokenRefreshResDto refreshAccessToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token.");
        }

        RefreshToken storedToken = findByToken(refreshToken) .orElseThrow(() -> new InvalidTokenException("Invalid or expired refresh token"));

        Member member = storedToken.getMember();

        String tokenSocialAccount = jwtUtil.extractSocialAccount(refreshToken);
        if (!tokenSocialAccount.equals(member.getSocialAccount())) {
            throw new InvalidTokenException("Token does not match with user.");
        }

        if (!"normal".equals(member.getStatus())) {
            throw new InvalidTokenException("Inactive or banned user.");
        }

        String newAccessToken = jwtUtil.generateToken(
            true,
            member.getSocialAccount(),
            member.getProvider(),
            member.getStatus(),
            "ROLE_" + member.getStatus()
        );

        return new TokenRefreshResDto(newAccessToken);
    }

}