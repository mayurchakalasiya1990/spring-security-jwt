package com.springsecuritywithjwt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
//OncePerRequestFilter -> GenericFilter -> Filter
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");
        final String jwt;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);// to do extract the user from JWT tocken

    }
}
