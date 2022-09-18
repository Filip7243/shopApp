package com.shopapp.shopApp.security;

import com.shopapp.shopApp.security.filter.CustomAuthorizationFilter;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AppUserServiceImpl userService;
    private final CustomPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final CustomLogoutHandler logoutHandler;

    @Bean
    public CustomAuthorizationFilter authorizationFilter() {
        return new CustomAuthorizationFilter(jwtUtils, userService);
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder.passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/auth/signIn").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/signUp").permitAll();
        http.authorizeRequests().antMatchers("/api/auth/confirm*").permitAll(); //TODO: i can remove it i think

        http.authorizeRequests().anyRequest().authenticated();

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout().logoutSuccessHandler(logoutHandler)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/api/auth/signIn");

        return http.build();
    }

}
