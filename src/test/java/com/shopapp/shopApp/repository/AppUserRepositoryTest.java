package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
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
public class AppUserRepositoryTest {
    @Autowired
    private AppUserRepository userRepo;
    @Autowired
    private AppUserRoleRepository roleRepo;

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
        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser).isInstanceOf(Optional.class);
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
        Optional<AppUser> foundUser = userRepo.findByEmail(anyString());
        assertThat(foundUser).isEmpty();
    }

    @Test
    void itShouldFindUserWithUserCode() {
        // given
        String userCode = UUID.randomUUID().toString();
        AppUser user = new AppUser(
                null,
                userCode,
                "John",
                "Doe",
                anyString(),
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

        AppUserRole role = new AppUserRole(null, "ROLE_SUPER_ADMIN", "SUPER_ADMIN");
        roleRepo.save(role);

        assert user.getRoles() != null;
        user.getRoles().add(role);
        userRepo.save(user);

        // when
        Optional<AppUser> foundUser = userRepo.findByUserCode(userCode);

        //then
        assertThat(foundUser).isPresent();
        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser).isInstanceOf(Optional.class);
    }

    @Test
    void itShouldNotFindUserWithUserCodeBecauseRolesAreEmpty() {
        // given
        String userCode = UUID.randomUUID().toString();
        AppUser user = new AppUser(
                null,
                userCode,
                "John",
                "Doe",
                anyString(),
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

        // when
        Optional<AppUser> foundUser = userRepo.findByUserCode(userCode);

        //then
        assertThat(foundUser).isNotPresent();
        assertThat(foundUser).isEmpty();
        assertThat(foundUser).isInstanceOf(Optional.class);
    }

    @Test
    void isShouldNotFindUserWithUserCodeBecauseUserCodeDoesNotExists() {
        Optional<AppUser> foundUser = userRepo.findByUserCode(anyString());

        assertThat(foundUser).isNotPresent();
        assertThat(foundUser).isEmpty();
        assertThat(foundUser).isInstanceOf(Optional.class);
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
    void itShouldCheckIfUserDoesNotExistsWithEmail() {
        // when
        Boolean expected = userRepo.existsByEmail(anyString());
        // then
        assertThat(expected).isFalse();
    }

    @Test
    void itShouldFindAllUsers() {
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                anyString(),
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

        AppUser userTwo = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                anyString(),
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
        assert userTwo.getRoles() != null;
        user.getRoles().add(role);
        userTwo.getRoles().add(role);

        userRepo.save(user);
        userRepo.save(userTwo);

        // when
        List<AppUser> allUsers = userRepo.findAll();
        int size = allUsers.size();
        // then
        assertThat(allUsers).isInstanceOf(List.class);
        assertThat(allUsers).isNotNull();
        assertThat(size).isNotZero();
        assertThat(size).isEqualTo(2);

    }

    @Test
    void itShouldNotReturnAnyUser() {
        // when
        List<AppUser> allUsers = userRepo.findAll();
        int size = allUsers.size();
        // then
        assertThat(allUsers.isEmpty()).isTrue();
        assertThat(size).isEqualTo(0);
        assertThat(size).isZero();
    }
    @Test
    void isShouldFindExpiredAccounts() {
        // given
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                anyString(),
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().minusMonths(1),
                false,
                false,
                true
        );

        userRepo.save(user);

        // when
        List<AppUser> expiredAccounts = userRepo.getExpiredAccounts(LocalDateTime.now());
        int size = expiredAccounts.size();

        // then
        assertThat(expiredAccounts).isInstanceOf(List.class);
        assertThat(expiredAccounts).isNotNull();
        assertThat(size).isNotZero();
        assertThat(size).isEqualTo(1);

    }

    @Test
    void itShouldNotFindAnyExpiredAccount() {
        // given
        AppUser user = new AppUser(
                null,
                UUID.randomUUID().toString(),
                "John",
                "Doe",
                anyString(),
                "1234",
                "123456789",
                "address",
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(1),
                false,
                false,
                true
        );

        userRepo.save(user);

        // when
        List<AppUser> expiredAccounts = userRepo.getExpiredAccounts(LocalDateTime.now());
        int size = expiredAccounts.size();

        // then
        assertThat(expiredAccounts).isInstanceOf(List.class);
        assertThat(expiredAccounts.isEmpty()).isTrue();
        assertThat(size).isZero();
        assertThat(size).isEqualTo(0);

    }
}
