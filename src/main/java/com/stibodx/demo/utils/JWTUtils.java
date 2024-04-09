package com.stibodx.demo.utils;

import com.stibodx.demo.entities.User;
import com.stibodx.demo.entities.Authorities;
import com.stibodx.demo.exceptions.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTUtils {
    public static String generateToken(UserAuth user, String secretKey) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            return Jwts.builder().setIssuer("stibodx").setSubject("JWT Token")
                    .claim("username", user.getUsername())
                    .claim("authorities", user.getAuthorities())
                    .claim("authoritiesIds", user.getAuthoritiesIds())
                    .claim("email", user.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))
                    .signWith(key).compact();
        }catch (Exception ex) {
            throw new JwtException("Error generating Jwt");
        }
    }

    public static Authentication validateJWT(String jwt, String secretKey) {
        LogInfo logInfo = new LogInfo(JWTUtils.class, "");
        try {
            logInfo.generateInfoLog("Validating jwt: ", jwt);
            jwt = jwt.replace("Bearer ", "");
            SecretKey key = Keys.hmacShaKeyFor(
                    secretKey.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            UserAuth userAuth = new UserAuth();

            claims.forEach((claimKey, value) ->  {
                switch (claimKey) {
                    case "username" -> {
                        ensureType(claimKey, value);
                        userAuth.setUsername((String) value);
                    }
                    case "email" -> {
                        ensureType(claimKey, value);
                        userAuth.setEmail((String) value);
                    }
                    case "authorities" -> {
                        ensureListType(claimKey, value, String.class);
                        userAuth.setAuthorities((List<String>) value);
                    }
                    case "authoritiesIds" -> {
                        ensureListType(claimKey, value, Integer.class);
                        userAuth.setAuthoritiesIds((List<Integer>) value);
                    }
                }
            });
            return new UsernamePasswordAuthenticationToken(userAuth, null,
                    getAuthorities(userAuth.getAuthorities()));
        } catch (JwtException e) {
            logInfo.generateInfoLog("Error validating Jwt");
            throw e;
        } catch (Exception e) {
            logInfo.generateInfoLog("Error validating Jwt");
            throw new JwtException("Error validating Jwt");
        }
    }

    private static List<GrantedAuthority> getAuthorities(List<String> authorities) {
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private static void ensureType(String key, Object value) {
        boolean isCorrectType = value instanceof String;
        if(!isCorrectType) {
            throw new JwtException("key: " + key + " is not a type of " + String.class.getCanonicalName());
        }
    }
    private static void ensureListType(String key, Object obj, Class<?> expectedType) {
        if (obj instanceof List<?> list) {
            if (!list.isEmpty() && !expectedType.isInstance(list.get(0))) {
                throw new JwtException("key: " + key + " is not a type of " + expectedType.getCanonicalName());
            }
        }
    }
}
