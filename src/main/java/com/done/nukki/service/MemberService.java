package com.done.nukki.service;

import com.done.nukki.entity.Member;
import com.done.nukki.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    // 생성자 주입
    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member create(String socialAccount, String provider) {
        return memberRepository.save(new Member(socialAccount, provider, "normal"));
    }

    public Optional<Member> findById(int id) {
        return memberRepository.findById((long) id);
    }

    public Member findBySocialAccountAndProviderOrCreate(String socialAccount, String provider) {
        return memberRepository.findBySocialAccountAndProvider(socialAccount, provider).orElseGet(() -> create(socialAccount, provider));
    }

    public Optional<Member> findBySocialAccount(String socialAccount) {
        return memberRepository.findBySocialAccount(socialAccount);
    }

    public Optional<Member> getCurrentMember() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("getCurrentMember username: " + username);
        return memberRepository.findBySocialAccount(username);
    }
}