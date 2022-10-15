package com.shopapp.shopApp.service;

import com.shopapp.shopApp.exception.token.PasswordResetTokenException;
import com.shopapp.shopApp.model.PasswordResetToken;
import com.shopapp.shopApp.repository.PasswordResetTokenRepository;
import com.shopapp.shopApp.service.resetpasswordtoken.PasswordResetTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.TOKEN_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepo;

    private PasswordResetTokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        this.tokenService = new PasswordResetTokenServiceImpl(tokenRepo);
    }

    @Test
    void canSaveToken() {
        PasswordResetToken token = any();
        tokenService.saveToken(token);
        verify(tokenRepo).save(token);
    }

    @Test
    void canDeleteToken() {
        var token = new PasswordResetToken();
        Long id = any();
        token.setId(id);

        when(tokenRepo.findById(id)).thenReturn(Optional.of(token));

        tokenService.deleteToken(id);
        verify(tokenRepo).delete(token);
    }

    @Test
    void throwsPasswordResetTokenExceptionWhenDeleteToken() {
        Long id = any();
        when(tokenRepo.findById(id)).thenReturn(Optional.empty());

        var exception =
                assertThrows(PasswordResetTokenException.class, () -> tokenService.deleteToken(id));
        assertThat(exception).isNotNull();
        assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }

    @Test
    void canGetToken() {
        var resetToken = new PasswordResetToken();
        var token = anyString();
        resetToken.setToken(token);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(resetToken));
        PasswordResetToken foundToken = tokenService.getToken(token);
        assertThat(foundToken).isNotNull();
    }

    @Test
    void throwsPasswordResetTokenExceptionWhenGetToken() {
        var token = anyString();

        when(tokenRepo.findByToken(token)).thenReturn(Optional.empty());
        var exception =
                assertThrows(PasswordResetTokenException.class, () -> tokenService.getToken(token));
        assertEquals(TOKEN_NOT_FOUND, exception.getMessage());
    }
}
