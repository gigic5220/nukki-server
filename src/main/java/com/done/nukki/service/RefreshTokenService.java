package com.done.nukki.service;

import com.done.nukki.dto.res.TokenRefreshResDto;
import com.done.nukki.entity.RefreshToken;
import com.done.nukki.entity.Member;
import com.done.nukki.exception.InvalidTokenException;
import com.done.nukki.exception.NotFoundException;
import com.done.nukki.repository.RefreshTokenRepository;
import com.done.nukki.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 리프레시 토큰 관련 비즈니스 로직을 담당하는 서비스.
 */
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    /**
     * RefreshTokenService 생성자.
     *
     * @param refreshTokenRepository 리프레시 토큰 저장소
     * @param memberService          회원 서비스
     * @param jwtUtil                JWT 유틸리티
     */
    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtUtil jwtUtil) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 특정 회원 ID에 대한 리프레시 토큰을 조회한다.
     *
     * @param memberId 조회할 회원 ID
     * @return 해당 회원의 리프레시 토큰
     * @throws NotFoundException 회원 ID에 해당하는 리프레시 토큰이 없을 경우 발생
     */
    public RefreshToken getByMemberId(int memberId) {
        return refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원 ID (" + memberId + ") 에 대한 리프레시 토큰을 찾을 수 없습니다."));
    }

    /**
     * 특정 리프레시 토큰 값을 기준으로 저장된 토큰을 조회한다.
     *
     * @param token 조회할 리프레시 토큰 값
     * @return 저장된 리프레시 토큰
     * @throws NotFoundException 해당 토큰이 존재하지 않을 경우 발생
     */
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("해당 리프레시 토큰 (" + token + ") 을 찾을 수 없습니다."));
    }

    /**
     * 리프레시 토큰을 저장하거나 기존 토큰을 업데이트한다.
     *
     * @param memberId         회원 ID
     * @param refreshTokenValue 저장할 리프레시 토큰 값
     */
    public void save(int memberId, String refreshTokenValue) {
        Member member = memberService.getById(memberId);

        try {
            RefreshToken refreshToken = getByMemberId(memberId);
            refreshToken.setToken(refreshTokenValue);
            refreshToken.setUpdated(LocalDateTime.now());
            refreshTokenRepository.save(refreshToken);
        } catch (NotFoundException e) {
            RefreshToken newToken = new RefreshToken(refreshTokenValue, member);
            refreshTokenRepository.save(newToken);
        }
    }

    /**
     * 리프레시 토큰을 검증하고, 새로운 액세스 토큰을 생성하여 반환한다.
     *
     * @param refreshToken 사용자가 제공한 리프레시 토큰
     * @return 새롭게 발급된 액세스 토큰
     * @throws InvalidTokenException 리프레시 토큰이 유효하지 않거나, 회원 정보가 일치하지 않거나, 계정이 비활성화된 경우 발생
     */
    public TokenRefreshResDto refreshAccessToken(String refreshToken) {

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new InvalidTokenException("유효하지 않은 리프레시 토큰입니다. (토큰 검증 실패)");
        }

        RefreshToken storedToken = getByToken(refreshToken);
        Member member = storedToken.getMember();

        String tokenSocialAccount = jwtUtil.extractSocialAccount(refreshToken);
        if (!tokenSocialAccount.equals(member.getSocialAccount())) {
            throw new InvalidTokenException("리프레시 토큰이 현재 사용자와 일치하지 않습니다. (회원 불일치)");
        }

        if (!"normal".equals(member.getStatus())) {
            throw new InvalidTokenException("해당 계정은 비활성화되었거나 차단되었습니다.");
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
