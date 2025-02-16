package com.done.nukki.security;

import com.done.nukki.entity.MemberDetailsWithMemberEntity;
import com.done.nukki.exception.InvalidTokenException;
import com.done.nukki.service.MemberService;
import com.done.nukki.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("doFilterInternal() request.getRequestURL(): " + request.getRequestURL());
        String token = extractToken(request);
        try {
            if (token != null) {
                Claims claims = jwtUtil.parseToken(token);

                String socialAccount = claims.getSubject();

                MemberDetailsWithMemberEntity memberDetailsWithMemberEntity = new MemberDetailsWithMemberEntity(memberService.findBySocialAccount(socialAccount));

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetailsWithMemberEntity, null, memberDetailsWithMemberEntity.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (InvalidTokenException e) {
            System.out.println("JWT 검증 실패: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }

}