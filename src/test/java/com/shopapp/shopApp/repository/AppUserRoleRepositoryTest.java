package com.shopapp.shopApp.repository;

import com.shopapp.shopApp.model.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AppUserRoleRepositoryTest {

    @Autowired
    private AppUserRoleRepository roleRepo;

    @BeforeEach
    void setUp() {
        // given
        AppUserRole roleOne = new AppUserRole(1L, "ROLE_SUPER_ADMIN", "SUPER_ADMIN");
        AppUserRole roleTwo = new AppUserRole(2L, "ROLE_ADMIN", "ADMIN");
        AppUserRole roleThree = new AppUserRole(3L, "ROLE_USER", "USER");

        roleRepo.save(roleOne);
        roleRepo.save(roleTwo);
        roleRepo.save(roleThree);
    }

    @Test
    void itShouldFindRoleWithName() {
        // when
        Optional<AppUserRole> role = roleRepo.findAppUserRoleByName("ROLE_SUPER_ADMIN");

        // then
        assertThat(role).isInstanceOf(Optional.class);
        assertThat(role).isPresent();
        assertThat(role).isNotNull();
        assertThat(role.get()).isInstanceOf(AppUserRole.class);
    }

    @Test
    void itShouldNotFindAnyRoleWithName() {

        // when
        Optional<AppUserRole> role = roleRepo.findAppUserRoleByName(anyString());

        // then
        assertThat(role).isNotPresent();
        assertThat(role).isEmpty();
    }

    @Test
    void itShouldCheckIfRoleExistsByName() {
        // when
        Boolean role = roleRepo.existsByName("ROLE_ADMIN");

        // then
        assertThat(role).isTrue();
    }

    @Test
    void itShouldCheckIfRoleDoesNotExistsByName() {
        // when
        Boolean role = roleRepo.existsByName("ROLE_NOT_EXISTS");

        // then
        assertThat(role).isFalse();
    }

    @Test
    void itShouldCheckIfRoleExistsById() {
        // when
        Boolean expected = roleRepo.existsById(1L);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckIfRoleDoesNotExistsById() {
        // when
        boolean expected = roleRepo.existsById(Long.MAX_VALUE);
        boolean expectedTwo = roleRepo.existsById(-1L);

        // then
        assertThat(expected).isFalse();
        assertThat(expectedTwo).isFalse();
    }
}