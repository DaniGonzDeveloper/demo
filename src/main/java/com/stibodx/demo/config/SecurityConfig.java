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


//    docker run --name postgres-db -e POSTGRES_PASSWORD=postgres-secret-test-key -p 5432:5432 -d cefd1c9e490c
//sudo ssh -i /Users/dan/Downloads/test-api-dani-key.pem ec2-user@13.53.124.152
//sftp -i /Users/dan/Downloads/test-api-dani-key.pem ec2-user@13.53.124.152
//put /ruta/local/archivo.txt /ruta/remota/


//life-style-app-1.0-SNAPSHOT.jar

//put /Users/dan/Downloads/springsecurity6-3.1.2/section9/life-style-app/target/life-style-app-1.0-SNAPSHOT-jar-with-dependencies.jar /home/ec2-user


//    INSERT INTO public.user_authorities (user_id,authorities_id) VALUES
//        (4,1),
//        (7,1);
//
//        INSERT INTO public.users ("name","password") VALUES
//        ('Dani','test'),
//        ('finalTest','test'),
//        ('Daniel','$2a$10$8auKZMux0ZAQIq1iT2AJ1OoXY8kinFh7Ke5ywz8R4IWJnkxSidsBS'),
//        ('newTest','$2a$10$ww.TcUqoN8WC2uGWkLwt6undOFt/P8kbVHt/lBp7xVcV2vlnN/5iC'),
//        ('newTest','$2a$10$fLyzjyeoRmnrPsdggspY/eQnsbtm5PVGZqiHx91.wyaJA8kbOLZa.'),
//        ('newTest','$2a$10$5EpBnnA04TTQTEDv6Bobw.otSYtWFI3h/vRvkDgabhN6ewbFa/e1y'),
//        ('kks@gmail.com','$2a$10$OxuQdTyaVKrRFaezbASFnepw7db9pm3B.c8VeTxnS.0uNwWEOI/ai');
//
//        INSERT INTO public.authorities ("role") VALUES
//        ('ROLE_normal');

//Construir imagen especificando el lugar del contenedor y con un nombre específico
//    sudo docker build --tag=life-style-app:latest . -f /home/ec2-user/java-container

//Arrancar un contenedor en un puerto a partir de una imagen
// sudo docker run -p8080:8080 life-style-app:latest

//Postgresql a partir de la imagen de postgress con el port mapping especificando una base de datos con contraseña
//Tambien se puede añadir un user name con -e POSTGRES_USER=miusuario y nombre de base de datos con -e POSTGRES_DB
//sudo docker run --name postgres-db -e POSTGRES_PASSWORD=postgres-secret-test-key -p 5432:5432 -d cefd1c9e490c
//Posible actualizacion con el docker compose:
//version: '2'
//        services:
//        postgres:
//        image: 'postgres:latest'
//        restart: always
//        volumes:
//        - './postgres_data:/var/lib/postgresql/data'
//        environment:
//        - POSTGRES_PASSWORD=secure_pass_here
//        ports:
//        - '5432:5432'

// ejecutar imagen con variables de entorno
//    sudo docker run -e JWT_KEY=jygEQeXHuPq2VdbyYFNkANdudQ53YEr2 -p8080:8080 life-style-app:latest
