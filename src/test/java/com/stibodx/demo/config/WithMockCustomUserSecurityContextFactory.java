package com.stibodx.demo.config;

import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.utils.UserAuth;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserAuth principal = UserAuth.builder()
                .username(customUser.name())
                .email(customUser.email())
                .authorities(Collections.emptyList())
                .build();
        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(principal, null, getAuthorities(principal.getAuthorities()));
        context.setAuthentication(auth);
        return context;
    }

    private static List<GrantedAuthority> getAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
