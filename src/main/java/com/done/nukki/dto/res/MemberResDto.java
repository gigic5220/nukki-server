package com.done.nukki.dto.res;
import com.done.nukki.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MemberResDto {
    private Integer id;
    private String nickname;
    private String provider;
    private String socialAccount;
    private String status;
    private LocalDateTime created;
    private LocalDateTime updated;

    public MemberResDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.provider = member.getProvider();
        this.socialAccount = member.getSocialAccount();
        this.status = member.getStatus();
        this.created = member.getCreated();
        this.updated = member.getUpdated();
    }
}
