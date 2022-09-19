package com.shopapp.shopApp.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shopapp.shopApp.model.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class JwtUtils {

    private String secret;
    private final List<String> blockedTokens = new ArrayList<>();

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

    public void getClaimsFromJwtToken(String token) {
        DecodedJWT decodedJWT = decodeJwt(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText("Bearer ") && authHeader.startsWith("Bearer ") && blockedTokens.contains(authHeader)) {
            return authHeader.substring("Bearer ".length());
        }
        return null;
    }

    public JwtResponse refreshAccessToken(String refreshToken, AppUser user) {
        String email = getUsernameFromJwtToken(refreshToken);

        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("authorities",
                        user.getAuthorities()
                                .stream().map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .withIssuer("issuer")
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 60 * 1000))
                .sign(algorithm);

        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        return new JwtResponse(
                email,
                accessToken,
                refreshToken,
                user.getUserCode(),
                authorities
        );
    }

    public void addToBlackList(String token) {
        this.blockedTokens.add(token);
    }

    private DecodedJWT decodeJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

}
