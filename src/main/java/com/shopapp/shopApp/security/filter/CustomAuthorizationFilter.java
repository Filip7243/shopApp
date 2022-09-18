package com.shopapp.shopApp.security.filter;

import com.auth0.jwt.interfaces.Claim;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@AllArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AppUserServiceImpl userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            log.info("PATH: " + request.getServletPath());
            if(request.getServletPath().equals("/api/auth/signIn")
                    || request.getServletPath().equals("/api/auth/signUp")
                    || request.getServletPath().equals("/api/auth/refresh")
                    || request.getServletPath().equals("/api/auth/confirm")
                    || request.getServletPath().equals("/api/users/accessToken/refresh")) {
                filterChain.doFilter(request, response);
            } else {
                String token = jwtUtils.getTokenFromHeader(request);
                if(token != null) {

                    String username = jwtUtils.getUsernameFromJwtToken(token);
                    AppUser user = (AppUser) userService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username, null, user.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
