package com.shopapp.shopApp.AppUserTests;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private AppUserRoleRepository roleRepo;

//    @BeforeEach
//    void setUp() {
//
//    }
//
//    @AfterEach
//    void tearDown() {
//    }

    @Test
    void itShouldFindUserWithEmail() {
        // given

        String email = "fipcio@interia.eu";
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                email,
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                true
        );

        AppUserRole role = new AppUserRole(null, "ROLE_SUPER_ADMIN", "SUPER ADMIN CAN DO EVERYTHING");
        roleRepo.save(role);

        assert user.getRoles() != null;
        user.getRoles().add(role);

        userRepo.save(user);
        // when
        Optional<AppUser> foundUser = userRepo.findByEmail(email);
        // then
        assertThat(foundUser).isPresent();
    }

    @Test
    void itShouldNotFindUserWithEmailBecauseRolesAreEmpty() {
        String email = "fipcio@interia.eu";
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                email,
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                true
        );

        userRepo.save(user);

        Optional<AppUser> foundUser = userRepo.findByEmail(email);

        assertThat(foundUser).isEmpty();
    }

    @Test
    void itShouldNotFindUserWithEmailBecauseEmailDoesNotExists() {
        String email = "fipcio@interia.eu";
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                email,
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                true
        );

        Optional<AppUser> foundUser = userRepo.findByEmail(anyString());

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findByUserCode() {
    }

    @Test
    void itShouldCheckIfUserExistsWithEmail() {
        // given
        String email = "fipcio@interia.eu";
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                email,
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                true
        );

        userRepo.save(user);
        // when
        Boolean expected = userRepo.existsByEmail(email);
        // then
        assertThat(expected).isTrue();
    }

    @Test
    void findAll() {
    }

    @Test
    void getExpiredAccounts() {
    }
}
