package com.done.nukki.controller;

import com.done.nukki.dto.req.LoginReqDto;
import com.done.nukki.dto.res.LoginResDto;
import com.done.nukki.service.AuthService;
import com.done.nukki.service.RefreshTokenService;
import com.done.nukki.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(JwtUtil jwtUtil, AuthService authService, RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public LoginResDto login(@Valid @RequestBody LoginReqDto dto) {
        return authService.login(dto.getSocialAccount(), dto.getProvider());
    }

    /*@PostMapping("/token/refresh")
    public TokenRefreshResDto refreshAccessToken(@Valid @RequestBody TokenRefreshReqDto dto) {

        System.out.println("refreshAccessToken: " + dto.getRefreshToken());

        Claims claims = refreshTokenService.validateRefreshToken(dto.getRefreshToken());

        String status = jwtUtil.extractStatus(dto.getRefreshToken());
        String provider = jwtUtil.extractProvider(dto.getRefreshToken());
        List<String> roleList = jwtUtil.extractRoles(dto.getRefreshToken());

        String newAccessToken = jwtUtil.generateToken(claims.getSubject(), provider, status, roleList);

        return new TokenRefreshResDto(
            newAccessToken
        );
    }*/
}