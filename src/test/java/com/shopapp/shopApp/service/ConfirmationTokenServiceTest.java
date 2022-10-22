package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.token.ConfirmationTokenConfirmedException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenExpiredException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
import com.shopapp.shopApp.service.confirmationtoken.ConfirmationTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConfirmationTokenServiceTest {

    @Mock
    private AppUserRepository userRepo;
    @Mock
    private ConfirmationTokenRepository tokenRepo;

    private ConfirmationTokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        this.tokenService = new ConfirmationTokenServiceImpl(tokenRepo, userRepo);
    }

    @Test
    void canSaveConfirmationToken() {
        ConfirmationToken token = any();

        tokenService.saveConfirmationToken(token);
        verify(tokenRepo).save(token);
    }

    @Test
    void canDeleteConfirmationTokenWithId() {
        var id = 1L;

        tokenService.deleteConfirmationTokenWithId(id);
        verify(tokenRepo).deleteById(id);
    }

    @Test
    void canFindConfirmationTokenByUser() {
        AppUser user = any();
        tokenService.findByUser(user);
        verify(tokenRepo).findByUser(user);
    }

    @Test
    void canGetConfirmationToken() {
        var confirmationToken = new ConfirmationToken();
        var token = anyString();
        confirmationToken.setToken(token);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(confirmationToken));
        ConfirmationToken foundToken = tokenService.getToken(token);

        verify(tokenRepo).findByToken(token);
        assertThat(foundToken).isNotNull();
        assertThat(foundToken).isInstanceOf(ConfirmationToken.class);
        assertThat(foundToken.getToken()).isEqualTo(token);
    }

    @Test
    void throwsConfirmationTokenNotFoundExceptionWhenGetConfirmationToken() {
        var token = anyString();

        when(tokenRepo.findByToken(token)).thenReturn(Optional.empty());
        var exception
                = assertThrows(ConfirmationTokenNotFoundException.class, () -> tokenService.getToken(token));

        verify(tokenRepo).findByToken(token);
        assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canConfirmEmail() {
        var confirmationToken = new ConfirmationToken();
        var token = anyString();
        AppUser user = new AppUser();
        confirmationToken.setUser(user);
        confirmationToken.setToken(token);
        confirmationToken.setIsConfirmed(false);
        confirmationToken.setExpiresAt(LocalDateTime.now().plusDays(1));

        when(tokenRepo.existsByToken(token)).thenReturn(true);

        tokenService.confirmEmail(confirmationToken);
        verify(tokenRepo).save(confirmationToken);
        verify(userRepo).save(confirmationToken.getUser());
    }

    @Test
    void throwsConfirmationTokenNotFoundExceptionWhenConfirmEmail() {
        var confirmationToken = new ConfirmationToken();
        var token = anyString();
        confirmationToken.setToken(token);

        when(tokenRepo.existsByToken(token)).thenReturn(false);

        var exception =
                assertThrows(ConfirmationTokenNotFoundException.class, () -> tokenService.confirmEmail(confirmationToken));
        assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }

    @Test
    void throwsConfirmationTokenConfirmedExceptionWhenConfirmEmail() {
        var confirmationToken = new ConfirmationToken();
        var token = anyString();
        confirmationToken.setToken(token);
        confirmationToken.setIsConfirmed(true);

        when(tokenRepo.existsByToken(token)).thenReturn(true);

        var exception =
                assertThrows(ConfirmationTokenConfirmedException.class, () -> tokenService.confirmEmail(confirmationToken));
        assertEquals(String.format(TOKEN_CONFIRMED, confirmationToken.getToken()), exception.getMessage());
    }

    @Test
    void throwsConfirmationTokenExpiredExceptionWhenConfirmEmail() {
        var confirmationToken = new ConfirmationToken();
        var token = anyString();
        confirmationToken.setToken(token);
        confirmationToken.setIsConfirmed(false);
        confirmationToken.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(tokenRepo.existsByToken(token)).thenReturn(true);

        var exception =
                assertThrows(ConfirmationTokenExpiredException.class, () -> tokenService.confirmEmail(confirmationToken));
        assertEquals(String.format(TOKEN_EXPIRED, confirmationToken.getExpiresAt().toString()), exception.getMessage());
    }
}
