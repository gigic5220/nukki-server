package com.done.nukki.dto.res;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenRefreshResDto {

    @NotBlank
    private String accessToken;
}
