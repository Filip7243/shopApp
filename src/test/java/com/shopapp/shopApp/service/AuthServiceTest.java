package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.email.EmailSenderImpl;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.exception.token.PasswordResetTokenException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.model.PasswordResetToken;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private PasswordResetTokenRepository passwordTokenRepo;
    private AppUserServiceImpl userService;
    private Authentication authentication;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() throws Exception {
        this.userRepo = Mockito.mock(AppUserRepository.class);
        this.jwtUtils = Mockito.mock(JwtUtils.class);
        this.emailSender = Mockito.mock(EmailSenderImpl.class);
        this.tokenService = Mockito.mock(ConfirmationTokenServiceImpl.class);
        this.passwordEncoder = Mockito.mock(CustomPasswordEncoder.class);
        this.bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
        this.passwordTokenRepo = Mockito.mock(PasswordResetTokenRepository.class);
        this.userService = Mockito.mock(AppUserServiceImpl.class);
        this.authentication = Mockito.mock(Authentication.class);
        this.authenticationManager = Mockito.mock(AuthenticationManager.class);

        this.authService =
                new AuthServiceImpl(userRepo, authenticationManager, jwtUtils, emailSender, tokenService, passwordEncoder, passwordTokenRepo, userService);
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

    @Test
    void canSignUpUser() {
        var user = new AppUser();
        user.setPassword("basdsa");
        var userDto = new AppUserSaveUpdateDto();
        userDto.setEmail("test@mail.com");

        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(false);
        when(userService.createUser(userDto)).thenReturn(user);
        when(passwordEncoder.passwordEncoder()).thenReturn(bCryptPasswordEncoder);

        authService.signUpUser(userDto);

        verify(tokenService).saveConfirmationToken(any());
        verify(emailSender).sendEmail(any(), any(), any());
        verify(userRepo).save(any());
    }

    @Test
    void throwsUserExistsExceptionWhenSignInUser() {
        var userDto = new AppUserSaveUpdateDto();
        userDto.setEmail("test@mail.com");

        when(userRepo.existsByEmail(userDto.getEmail())).thenReturn(true);

        var exception =
                assertThrows(UserExistsException.class, () -> authService.signUpUser(userDto));
        assertEquals(String.format(USER_ALREADY_EXISTS, userDto.getEmail()), exception.getMessage());
        assertThat(exception).isInstanceOf(UserExistsException.class);
    }

    @Test
    void canForgetPassword() {
        when(userRepo.findByEmail(any())).thenReturn(Optional.of(new AppUser()));
        authService.forgetPassword("test");

        verify(passwordTokenRepo).save(any());
        verify(emailSender).sendEmail(any(), any(), any());
    }

    @Test
    void throwsUserNotFoundExceptionWhenForgetPassword() {
        var email = anyString();

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());
        UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> authService.forgetPassword(email));

        assertEquals(String.format(USER_NOT_FOUND, email), exception.getMessage());
        assertThat(exception).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void canResetPassword() {
        var token = new PasswordResetToken();
        var user = new AppUser();
        token.setToken("test");
        token.setIsPasswordReset(false);
        token.setExpiresAt(LocalDateTime.now().plusDays(1));
        token.setUser(user);

        when(passwordTokenRepo.findByToken("test")).thenReturn(Optional.of(token));
        when(passwordEncoder.passwordEncoder()).thenReturn(bCryptPasswordEncoder);
        authService.resetPassword(token.getToken());

        verify(passwordEncoder.passwordEncoder()).encode("password-from-input");
        assertTrue(token.getIsPasswordReset());
        verify(passwordTokenRepo).save(token);
        verify(userRepo).save(user);
    }

    @Test
    void throwsConfirmationTokenNotFoundExceptionWhenResetPassword() {
        when(passwordTokenRepo.findByToken("test")).thenReturn(Optional.empty());
        var exception = assertThrows(ConfirmationTokenNotFoundException.class, () -> authService.resetPassword("test"));

        assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
        assertThat(exception).isInstanceOf(ConfirmationTokenNotFoundException.class);
    }

    @Test
    void throwsPasswordResetTokenExceptionIfTokenAlreadyUsedWhenResetPassword() {
        var token = new PasswordResetToken();
        token.setToken("test");
        token.setIsPasswordReset(true);

        when(passwordTokenRepo.findByToken("test")).thenReturn(Optional.of(token));
        var exception =
                assertThrows(PasswordResetTokenException.class, () -> authService.resetPassword("test"));

        assertEquals("Token already used", exception.getMessage());
        assertThat(exception).isInstanceOf(PasswordResetTokenException.class);
    }

    @Test
    void throwsPasswordResetTokenExceptionIfTokenExpiredWhenResetPassword() {
        var token = new PasswordResetToken();
        token.setToken("test");
        token.setIsPasswordReset(false);
        token.setExpiresAt(LocalDateTime.now().minusDays(1));
        when(passwordTokenRepo.findByToken("test")).thenReturn(Optional.of(token));
        var exception =
                assertThrows(PasswordResetTokenException.class, () -> authService.resetPassword("test"));
        assertEquals("Token already expired!", exception.getMessage());
        assertThat(exception).isInstanceOf(PasswordResetTokenException.class);
    }


}
