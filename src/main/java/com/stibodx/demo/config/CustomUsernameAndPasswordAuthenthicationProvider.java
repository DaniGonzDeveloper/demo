package com.stibodx.demo.config;

import com.stibodx.demo.controller.mapper.UserAuthMapper;
import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.entities.User;
import com.stibodx.demo.service.UserService;
import com.stibodx.demo.utils.UserAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class CustomUsernameAndPasswordAuthenthicationProvider implements AuthenticationProvider {
    UserService userService;
    PasswordEncoder passwordEncoder;
    UserAuthMapper userAuthMapper;
    @Autowired
    CustomUsernameAndPasswordAuthenthicationProvider(UserService userService, PasswordEncoder passwordEncoder,
                                                     UserAuthMapper userAuthMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userAuthMapper = userAuthMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User user = this.userService.findByNameNoRoles(authentication.getName());
        if (Objects.isNull(user)) {
            throw new BadCredentialsException("User details not found for user: " + authentication.getName());
        } else if (passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
            UserAuth userAuth = userAuthMapper.userEntityToUserAuth(user);
            return new UsernamePasswordAuthenticationToken(userAuth, null, getAuthorities(user.getAuthorities()));
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }
    private List<GrantedAuthority> getAuthorities(List<Authorities> authoritiesDB) {
        return authoritiesDB.stream().map(authority -> new SimpleGrantedAuthority(authority.getRole())).collect(Collectors.toList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
