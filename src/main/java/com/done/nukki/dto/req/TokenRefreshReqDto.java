package com.done.nukki.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TokenRefreshReqDto {

    @NotBlank
    private String refreshToken;
}
