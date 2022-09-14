package com.shopapp.shopApp.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shopapp.shopApp.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtils {


    private String secret;

    @Value("${jwt.token.secret}")
    private void setSecret(String secret) {
        this.secret = secret;
    }

    public String generateJwtAccessToken(Authentication authentication) {

        AppUser user = (AppUser) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());

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

    public String generateJwtRefreshToken(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());

        log.info("Generating refresh token");

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("issuer")
                .withExpiresAt(new Date(System.currentTimeMillis() + 240 * 60 * 1000))
                .sign(algorithm);
    }

    public String getUsernameFromJwtToken(String token) {
        DecodedJWT decodedJWT = decodeJwt(token);
        return decodedJWT.getSubject();
    }

    public Map<String, Claim> getClaimsFromJwtToken(String token) {
        DecodedJWT decodedJWT = decodeJwt(token);
        return decodedJWT.getClaims();
    }

    private DecodedJWT decodeJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
