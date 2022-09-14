package com.shopapp.shopApp.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shopapp.shopApp.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class JwtUtils {

    @Value("${jwt.token.secret}")
    private static String secret;

    public static String generateJwtAccessToken(Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512("secret".getBytes());

        log.info("Generating access token");

        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("authorities",
                        user.getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withIssuer("issuer")
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000))
                .sign(algorithm);
    }

    public static String generateJwtRefreshToken(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512("secret".getBytes());

        log.info("Generating refresh token");

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("issuer")
                .withExpiresAt(new Date(System.currentTimeMillis() + 240 * 60 * 1000))
                .sign(algorithm);
    }

    public static String getUsernameFromJwtToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

}
