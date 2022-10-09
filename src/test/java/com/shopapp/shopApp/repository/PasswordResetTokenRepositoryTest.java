package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.PasswordResetToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class PasswordResetTokenRepositoryTest {

    @Autowired
    private PasswordResetTokenRepository tokenRepo;
    @Autowired
    private AppUserRepository userRepo;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        AppUser user = AppUser.builder()
                .id(null)
                .userCode(UUID.randomUUID().toString())
                .name("testName")
                .lastName("testLastName")
                .email("test@mail.com")
                .password("testPassword!")
                .phoneNumber("+48001111212")
                .address("testAddress")
                .roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusYears(6))
                .isLocked(false)
                .isCredentialsExpired(false)
                .isEnabled(false)
                .build();

        userRepo.save(user);

        this.token = new PasswordResetToken(
                null, UUID.randomUUID().toString(),
                LocalDateTime.now().plusMinutes(30),
                false,
                user
        );

        tokenRepo.save(token);

    }

    @Test
    void itShouldFindPasswordResetTokenByToken() {
        // when
        Optional<PasswordResetToken> foundToken = tokenRepo.findByToken(token.getToken());

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken).isInstanceOf(Optional.class);
        assertThat(foundToken.get()).isInstanceOf(PasswordResetToken.class);
    }

    @Test
    void itShouldNotFindPasswordResetTokenByToken() {
        // when
        Optional<PasswordResetToken> foundToken = tokenRepo.findByToken(anyString());

        // then
        assertThat(foundToken).isNotPresent();
        assertThat(foundToken).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldFindExpiredPasswordResetTokens() {
        // when
        List<PasswordResetToken> foundTokens = tokenRepo.getExpiredTokens(LocalDateTime.now().plusDays(1));

        // then
        assertThat(foundTokens.size()).isNotNull();
        assertThat(foundTokens.size()).isNotZero();
    }

    @Test
    void itShouldNotFindExpiredPasswordResetTokens() {
        // when
        List<PasswordResetToken> foundTokens = tokenRepo.getExpiredTokens(LocalDateTime.now());

        // then
        assertThat(foundTokens.size()).isZero();
    }

    @Test
    void itShouldFindUsedPasswordResetTokens() {
        // given
        token.setIsPasswordReset(true);
        tokenRepo.save(token);
        // when
        List<PasswordResetToken> foundTokens = tokenRepo.getUsedTokens();

        // then
        assertThat(foundTokens).isNotNull();
        assertThat(foundTokens.size()).isNotZero();
    }

    @Test
    void itShouldNotFindUsedPasswordResetTokens() {
        // when
        List<PasswordResetToken> foundTokens = tokenRepo.getUsedTokens();

        // then
        assertThat(foundTokens.size()).isZero();
    }
}
