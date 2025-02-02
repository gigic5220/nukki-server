package com.done.nukki.repository;

import com.done.nukki.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialAccount(String socialAccount);
    Optional<Member> findBySocialAccountAndProvider(String socialAccount, String provider);
}