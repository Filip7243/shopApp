package AppUserTests;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.ConfirmationTokenRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ConfirmationTokenRepositoryTest {

    private AppUser user;
    private ConfirmationToken confirmedToken;
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private ConfirmationTokenRepository tokenRepo;

    @BeforeEach
    void setUp() {
        this.user = AppUser.builder()
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

        this.confirmedToken = new ConfirmationToken(
                null, UUID.randomUUID().toString(),
                LocalDateTime.now(), LocalDateTime.now().plusMinutes(30),
                true, user
        );

        userRepo.save(user);
        tokenRepo.save(confirmedToken);
    }

    @Test
    void itShouldFindConfirmationTokenByUser() {
        // when
        List<ConfirmationToken> foundTokens = tokenRepo.findByUser(user);

        // then
        assertThat(foundTokens).isNotNull();
        assertThat(foundTokens.size()).isGreaterThan(0);
    }

    @Test
    void itShouldNotFindConfirmationTokenByUser() {
        // when
        List<ConfirmationToken> foundTokens = tokenRepo.findByUser(any());

        // then
        assertThat(foundTokens.size()).isEqualTo(0);
    }

    @Test
    void itShouldFindConfirmationTokenByToken() {
        // when
        Optional<ConfirmationToken> foundToken = tokenRepo.findByToken(confirmedToken.getToken());

        // then
        assertThat(foundToken).isPresent();
        assertThat(foundToken).isNotEmpty();
        assertThat(foundToken.get()).isInstanceOf(ConfirmationToken.class);
    }

    @Test
    void itShouldNotFindAnyConfirmationTokenByToken() {
        // when
        Optional<ConfirmationToken> foundToken = tokenRepo.findByToken(anyString());

        // then
        assertThat(foundToken).isNotPresent();
        assertThat(foundToken).isEmpty();
    }

    @Test
    void itShouldCheckIfConfirmationTokenExistsByToken() {
        // when
        Boolean expected = tokenRepo.existsByToken(confirmedToken.getToken());

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfConfirmationTokenDoesNotExistsByToken() {
        // when
        Boolean expected = tokenRepo.existsByToken(anyString());

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldFindExpiredTokens() {
        // when
        List<ConfirmationToken> expiredTokens = tokenRepo.getExpiredTokens(LocalDateTime.now().plusDays(1));

        // then
        assertThat(expiredTokens.size()).isGreaterThan(0);
    }

    @Test
    void itShouldNotFindExpiredTokens() {
        // when
        List<ConfirmationToken> expiredTokens = tokenRepo.getExpiredTokens(LocalDateTime.now().minusDays(1));

        // then
        assertThat(expiredTokens.size()).isZero();
    }

    @Test
    void itShouldFindConfirmedTokens() {
        // when
        List<ConfirmationToken> confirmedTokens = tokenRepo.getConfirmedTokens();

        // then
        assertThat(confirmedTokens.size()).isGreaterThan(0);
    }

    @Test
    void itShouldNotFindConfirmedTokens() {
        // given
        confirmedToken.setIsConfirmed(false);
        tokenRepo.save(confirmedToken);

        // when
        List<ConfirmationToken> confirmedTokens = tokenRepo.getConfirmedTokens();

        // then
        assertThat(confirmedTokens.size()).isZero();
    }
}