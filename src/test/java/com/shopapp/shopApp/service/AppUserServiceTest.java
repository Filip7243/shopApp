package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import com.shopapp.shopApp.security.CustomPasswordEncoder;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {

    @Mock
    private AppUserRepository userRepo;
    @Mock
    private AppUserRoleRepository roleRepo;

    private AppUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        this.userService = new AppUserServiceImpl(userRepo, roleRepo, new CustomPasswordEncoder());
    }

    @Test
    void canGetAllUsersWithRolesJoined() {
        // when
        userService.getUsers();
        // then
        verify(userRepo).findAll();
    }

    @Test
    void canLoadUserWithUsername() {
        AppUser user = new AppUser();
        user.setEmail("filip@mail.com");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("filip@mail.com");

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
    }

    @Test
    void throwsExceptionWhenLoadUserWithUsername() {
        String email = anyString();
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.loadUserByUsername(email));
        assertEquals("User " + email + " not found!", exception.getMessage());
    }

    @Test
    void canCreateUser() {
        String email = anyString();
        String roleName = "ROLE_USER";

        AppUserSaveUpdateDto newUser = new AppUserSaveUpdateDto();
        newUser.setEmail(email);
        newUser.setPassword("1234");

        when(userRepo.existsByEmail(email)).thenReturn(false);
        when(roleRepo.findAppUserRoleByName(roleName)).thenReturn(Optional.of(new AppUserRole()));

        AppUser user = userService.createUser(newUser);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
    }

    @Test
    void throwsUserExistsExceptionWhenCreateUser() {
        String email = anyString();

        AppUserSaveUpdateDto newUser = new AppUserSaveUpdateDto();
        newUser.setEmail(email);
        newUser.setPassword("1234");

        when(userRepo.existsByEmail(email)).thenReturn(true);

        UserExistsException exception = assertThrows(UserExistsException.class, () -> userService.createUser(newUser));

        assertEquals(String.format("User with email: %s, already exists!", email), exception.getMessage());
    }

    @Test
    void throwsRoleNotFoundExceptionWhenCreateUser() {
        // given
        AppUserSaveUpdateDto user = new AppUserSaveUpdateDto();
        // when
        RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () -> userService.createUser(user));
        // then
        assertEquals("Role ROLE_USER not found!", exception.getMessage());
    }


}
