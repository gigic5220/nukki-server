package com.done.nukki.controller;

import com.done.nukki.dto.req.LoginReqDto;
import com.done.nukki.dto.req.TokenRefreshReqDto;
import com.done.nukki.dto.res.LoginResDto;
import com.done.nukki.dto.res.TokenRefreshResDto;
import com.done.nukki.service.AuthService;
import com.done.nukki.service.RefreshTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public LoginResDto login(@Valid @RequestBody LoginReqDto dto) {
        return authService.login(dto.getSocialAccount(), dto.getProvider());
    }

    @PostMapping("/token/refresh")
    public TokenRefreshResDto refreshAccessToken(@Valid @RequestBody TokenRefreshReqDto dto) {
       return refreshTokenService.refreshAccessToken(dto.getRefreshToken());
    }
}