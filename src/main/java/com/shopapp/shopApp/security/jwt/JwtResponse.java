package com.shopapp.shopApp.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private String username;
    private String accessToken;
    private String refreshToken;
    private String userCode;
    private List<String> authorities;

}
