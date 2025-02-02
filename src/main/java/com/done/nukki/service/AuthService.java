package com.done.nukki.service;
import com.done.nukki.dto.res.LoginResDto;
import com.done.nukki.dto.res.MemberResDto;
import com.done.nukki.entity.Member;
import com.done.nukki.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;

    // 생성자 주입
    @Autowired
    public AuthService(JwtUtil jwtUtil, MemberService memberService, RefreshTokenService refreshTokenService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public LoginResDto login(String socialAccount, String provider) {
        Member member = memberService.findBySocialAccountAndProviderOrCreate(socialAccount, provider);

        String accessToken = jwtUtil.generateToken(
        true,
            member.getSocialAccount(),
            member.getProvider(),
            member.getStatus(),
            "ROLE_" + member.getStatus().toUpperCase()
        );

        String refreshToken = jwtUtil.generateToken(
            false,
            member.getSocialAccount(),
            member.getProvider(),
            member.getStatus(),
            "ROLE_" + member.getStatus().toUpperCase()
        );

        refreshTokenService.save(member.getId(), refreshToken);

        long accessTokenExpireAt = jwtUtil.getExpirationInMillis(accessToken);

        return new LoginResDto(
            accessToken,
            accessTokenExpireAt,
            refreshToken,
            new MemberResDto(member)
        );
    }
}