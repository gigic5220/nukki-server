package com.done.nukki.service;

import com.done.nukki.entity.Member;
import com.done.nukki.exception.NotFoundException;
import com.done.nukki.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 회원 관련 비즈니스 로직을 담당하는 서비스.
 */
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 생성자 주입
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 새로운 회원을 생성하여 저장합니다.
     * @param socialAccount 소셜 계정 ID
     * @param provider 소셜 로그인 제공자 (예: kakao, google)
     * @return 저장된 회원 엔티티
     */
    public Member create(String socialAccount, String provider) {
        return memberRepository.save(new Member(socialAccount, provider, "normal"));
    }

    /**
     * 특정 회원 ID로 회원 정보를 조회합니다.
     * @param id 조회할 회원의 ID
     * @return 해당 회원 엔티티
     * @throws NotFoundException 회원이 존재하지 않을 경우 발생
     */
    public Member getById(int id) {
        return memberRepository.findById((long) id)
                .orElseThrow(() -> new NotFoundException("회원 ID (" + id + ") 에 해당하는 회원을 찾을 수 없습니다."));
    }

    /**
     * 소셜 계정과 제공자로 회원을 조회하며, 존재하지 않으면 새로 생성합니다.
     * @param socialAccount 소셜 계정 ID
     * @param provider 소셜 로그인 제공자
     * @return 기존 회원 또는 새로 생성된 회원 엔티티
     */
    public Member findBySocialAccountAndProviderOrCreate(String socialAccount, String provider) {
        return memberRepository.findBySocialAccountAndProvider(socialAccount, provider)
                .orElseGet(() -> create(socialAccount, provider));
    }

    /**
     * 소셜 계정 ID로 회원을 조회합니다.
     * @param socialAccount 조회할 소셜 계정 ID
     * @return 해당 회원 엔티티
     * @throws NotFoundException 해당 소셜 계정이 존재하지 않을 경우 발생
     */
    public Member findBySocialAccount(String socialAccount) {
        return memberRepository.findBySocialAccount(socialAccount)
                .orElseThrow(() -> new NotFoundException("소셜 계정 (" + socialAccount + ") 에 해당하는 회원을 찾을 수 없습니다."));
    }
}
