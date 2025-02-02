package com.done.nukki.dto.res;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResDto {
    private String accessToken;
    private long accessTokenExpires;
    private String refreshToken;
    private MemberResDto member;
}
