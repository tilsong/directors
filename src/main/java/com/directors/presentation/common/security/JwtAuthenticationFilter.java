package com.directors.presentation.common.security;

import com.directors.infrastructure.auth.JwtAuthenticationManager;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticationManager jm;

    private final List<String> excludedPaths = List.of("/user/logIn", "/user/signUp");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (excludedPaths.contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰을 통해 인증 확인
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 인증 데이터입니다.");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
            Authentication authentication = jm.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), null, authentication.getAuthorities()));
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 실패했습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
