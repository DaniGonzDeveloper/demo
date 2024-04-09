package com.stibodx.demo.config;

import com.stibodx.demo.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private final String secretKey;
    public JWTTokenValidatorFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeaderValue = "Authorization";
        String jwt = request.getHeader(authorizationHeaderValue);
        if (null != jwt) {
            try {
                Authentication auth = JWTUtils.validateJWT(jwt, secretKey);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/logIn") || request.getServletPath().startsWith("/signIn") ||
                request.getServletPath().startsWith("/v2/api-docs") || request.getServletPath().startsWith("/swagger-ui");
    }

}
