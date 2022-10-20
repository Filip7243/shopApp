package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.email.EmailSenderImpl;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.PasswordResetTokenRepository;
import com.shopapp.shopApp.security.CustomPasswordEncoder;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import com.shopapp.shopApp.service.auth.AuthServiceImpl;
import com.shopapp.shopApp.service.confirmationtoken.ConfirmationTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    private AppUserRepository userRepo;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private EmailSenderImpl emailSender;
    private ConfirmationTokenServiceImpl tokenService;
    private CustomPasswordEncoder passwordEncoder;
    private PasswordResetTokenRepository passwordTokenRepo;
    private AppUserServiceImpl userService;
    private Authentication authentication;
    private UsernamePasswordAuthenticationToken token;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() throws Exception {
        this.userRepo = Mockito.mock(AppUserRepository.class);
        this.jwtUtils = Mockito.mock(JwtUtils.class);
        this.emailSender = Mockito.mock(EmailSenderImpl.class);
        this.tokenService = Mockito.mock(ConfirmationTokenServiceImpl.class);
        this.passwordEncoder = Mockito.mock(CustomPasswordEncoder.class);
        this.passwordTokenRepo = Mockito.mock(PasswordResetTokenRepository.class);
        this.userService = Mockito.mock(AppUserServiceImpl.class);
        this.authentication = Mockito.mock(Authentication.class);
        this.token = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        this.authenticationManager = Mockito.mock(AuthenticationManager.class);


        this.authService = new AuthServiceImpl(userRepo, authenticationManager, jwtUtils, emailSender, tokenService, passwordEncoder, passwordTokenRepo, userService);
    }

    @Test
    void canSignInUser() {//todo;
        var anyString = anyString();

        var loginRequest = new LoginRequest(anyString, anyString);

        var user = new AppUser();
        user.setEmail(loginRequest.getEmail());
        user.setRoles(new HashSet<>());
        assert user.getRoles() != null;
        user.getRoles().add(new AppUserRole(null, "ROLE_USER", "USER"));

        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtUtils.generateJwtAccessToken(authentication)).thenReturn(anyString);
        when(jwtUtils.generateJwtRefreshToken(authentication)).thenReturn(anyString);
        when(jwtUtils.getUsernameFromJwtToken(anyString)).thenReturn(anyString);

        JwtResponse response = authService.signInUser(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(JwtResponse.class);
        assertThat(response.getUsername()).isEqualTo(loginRequest.getEmail());
        assertEquals(anyString, response.getAccessToken());
        assertEquals(anyString, response.getRefreshToken());
        assertThat(response.getAuthorities()).isNotNull();
        assertThat(response.getAuthorities().size()).isEqualTo(1);
    }

    @Test
    void canNotSignInUserBecauseUserDoesNotExist() {
        String msg = "Bad credentials";

        when(authenticationManager.authenticate(any())).thenThrow(new InternalAuthenticationServiceException(msg));

        var ex =
                assertThrows(InternalAuthenticationServiceException.class,
                        () -> authService.signInUser(new LoginRequest("ab", "cd")));

        assertThat(ex).isNotNull();
        assertThat(ex).isInstanceOf(InternalAuthenticationServiceException.class);
        assertThat(ex.getMessage()).isEqualTo(msg);
    }


}
