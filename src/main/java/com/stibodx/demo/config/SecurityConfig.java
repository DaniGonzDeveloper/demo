package com.stibodx.demo.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.lang.reflect.Array;
import java.util.*;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {
    @Value("#{'${myapp.security.pathRules}'.split(',')}")
    private List<String> pathRules;
    @Value("${myapp.security.secretkey}")
    private String secretKey;
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .cors().configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Collections.singletonList("*"));
                        config.setAllowedMethods(Collections.singletonList("*"));
//                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Collections.singletonList("Authorization"));
                        config.setMaxAge(3600L);
                        return config;

                    }
                }).and().csrf().disable()
                .addFilterBefore(new JWTTokenValidatorFilter(secretKey), BasicAuthenticationFilter.class);

//        Make the auth exceptions be managed by the defined exception handler
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    resolver.resolveException(request, response, null, authException);
                });

        //Make the access denied exceptions be managed by the defined exception handler
        http.exceptionHandling()
                .accessDeniedHandler((request, response, authException) -> {
                    resolver.resolveException(request, response, null, authException);
                });

        Map<String, String[]> pathRoleMap = new HashMap<>();

        for (String rule : pathRules) {
            String[] parts = rule.split("=");
            if (parts.length == 2) {
                String path = parts[0];
                String[] roles = parts[1].split("-");
                pathRoleMap.put(path, roles);
            }
        }

        http.authorizeHttpRequests((request) -> {
            for (Map.Entry<String, String[]> entry : pathRoleMap.entrySet()) {
                if(Objects.isNull(entry.getValue())) {
                    String[] normalRole = {"normal"};
                    entry.setValue(normalRole);
                }
                request.requestMatchers(entry.getKey()).hasAnyRole(entry.getValue());
            }
            request.requestMatchers("/v3/api-docs/**",
                    "/swagger-ui/**", "/swagger-ui.html").permitAll();
            request.requestMatchers("/signIn").permitAll();

            request.anyRequest().authenticated();
        });

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
