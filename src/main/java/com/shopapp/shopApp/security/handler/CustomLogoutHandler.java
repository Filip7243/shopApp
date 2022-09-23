package com.shopapp.shopApp.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@AllArgsConstructor
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final JwtUtils jwtUtils;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String token = jwtUtils.getTokenFromHeader(request);
        jwtUtils.addToBlackList(token);
        response.setHeader(AUTHORIZATION, null);
        super.onLogoutSuccess(request, response, authentication);
    }
}
