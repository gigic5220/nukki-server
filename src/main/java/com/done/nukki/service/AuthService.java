package com.done.nukki.service;

import com.done.nukki.dto.res.LoginResDto;
import com.done.nukki.dto.res.MemberResDto;
import com.done.nukki.entity.Member;
import com.done.nukki.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 인증 및 로그인 관련 비즈니스 로직을 담당하는 서비스.
 */
@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    /**
     * AuthService 생성자.
     *
     * @param jwtUtil             JWT 토큰 관련 유틸리티 클래스
     * @param memberService       회원 관련 비즈니스 로직을 처리하는 서비스
     * @param refreshTokenService 리프레시 토큰 관련 비즈니스 로직을 처리하는 서비스
     */
    @Autowired
    public AuthService(JwtUtil jwtUtil, MemberService memberService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
    }

    /**
     * 사용자의 로그인 또는 회원가입을 처리하고, JWT 액세스 토큰과 리프레시 토큰을 발급한다.
     *
     * @param socialAccount 소셜 계정 ID (이메일 또는 소셜 제공자가 제공하는 고유 ID)
     * @param provider      소셜 로그인 제공자 (예: kakao, google)
     * @return 로그인 응답 DTO (액세스 토큰, 리프레시 토큰 및 회원 정보 포함)
     */
    @Transactional
    public LoginResDto login(String socialAccount, String provider) {
        // 기존 회원 조회 또는 신규 회원 생성
        Member member = memberService.findBySocialAccountAndProviderOrCreate(socialAccount, provider);

        // 액세스 토큰 생성
        String accessToken = jwtUtil.generateToken(
            true,
            member.getSocialAccount(),
            member.getProvider(),
            member.getStatus(),
            "ROLE_" + member.getStatus().toUpperCase()
        );

        // 리프레시 토큰 생성
        String refreshToken = jwtUtil.generateToken(
            false,
            member.getSocialAccount(),
            member.getProvider(),
            member.getStatus(),
            "ROLE_" + member.getStatus().toUpperCase()
        );

        // 리프레시 토큰 저장
        refreshTokenService.save(member.getId(), refreshToken);

        // 액세스 토큰 만료 시간 조회
        long accessTokenExpireAt = jwtUtil.getExpirationInMillis(accessToken);

        return new LoginResDto(
            accessToken,
            accessTokenExpireAt,
            refreshToken,
            new MemberResDto(member)
        );
    }

    /**
     * 현재 로그인한 사용자의 정보를 가져온다.
     * Spring Security의 `SecurityContextHolder`에서 인증된 사용자의 이름을 가져와 해당 회원 정보를 조회한다.
     *
     * @return 현재 로그인한 회원 엔티티
     */
    public Member getSecurityContextMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberService.findBySocialAccount(username);
    }
}
