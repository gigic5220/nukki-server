package com.done.nukki.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginReqDto {

    @NotBlank
    private String socialAccount;

    @NotBlank
    private String provider;
}
