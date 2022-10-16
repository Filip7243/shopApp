package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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
        String email = "filip@mail.com";
        user.setEmail(email);
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        verify(userRepo).findByEmail(email);
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
        RoleNotFoundException exception =
                assertThrows(RoleNotFoundException.class, () -> userService.createUser(user));
        // then
        assertEquals("Role ROLE_USER not found!", exception.getMessage());
    }

    @Test
    void canGetUserWithUserCode() {
        AppUser user = new AppUser();
        String uuid = anyString();
        user.setUserCode(uuid);
        when(userRepo.findByUserCode(user.getUserCode())).thenReturn(Optional.of(user));

        AppUser foundUser = userService.getUserWithUserCode(user.getUserCode());

        assertNotNull(foundUser);
        assertEquals(foundUser.getUserCode(), user.getUserCode());
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenGetUserWithUserCode() {
        String uuid = anyString();
        when(userRepo.findByUserCode(uuid)).thenReturn(Optional.empty());

        UserCodeNotFoundException exception =
                assertThrows(UserCodeNotFoundException.class, () -> userService.getUserWithUserCode(uuid));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, uuid));
    }

    @Test
    void canDeleteUserWithUserCode() {
        AppUser user = new AppUser();
        String uuid = anyString();
        user.setUserCode(uuid);

        when(userRepo.findByUserCode(uuid)).thenReturn(Optional.of(user));
        userService.deleteUserWithUserCode(uuid);

        verify(userRepo).delete(user);
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenDeleteUserWithUserCode() {
        String uuid = anyString();
        when(userRepo.findByUserCode(uuid)).thenReturn(Optional.empty());

        UserCodeNotFoundException exception =
                assertThrows(UserCodeNotFoundException.class, () -> userService.getUserWithUserCode(uuid));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, uuid));
    }

    @Test
    void canUpdateUser() {
        AppUser user = new AppUser();
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail(anyString)).thenReturn(true);

        userService.updateUser(anyString,
                new AppUserSaveUpdateDto(anyString, anyString, anyString, anyString, anyString, anyString));

        verify(userRepo).save(user);
    }

    @Test
    void canUpdateUserButNotEmailBecauseAlreadyExists() {
        AppUser user = new AppUser();
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(userRepo.existsByEmail(anyString)).thenReturn(false);

        userService.updateUser(anyString,
                new AppUserSaveUpdateDto(anyString, anyString, anyString, anyString, anyString, anyString));

        verify(userRepo).save(user);
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenUpdateUser() {
        String code = anyString();

        when(userRepo.findByUserCode(code)).thenReturn(Optional.empty());
        UserCodeNotFoundException exception = assertThrows(UserCodeNotFoundException.class,
                () -> userService.updateUser(code, ArgumentMatchers.any(AppUserSaveUpdateDto.class)));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, code));
    }

    @Test
    void canAddRoleToUser() {
        AppUser user = new AppUser();
        AppUserRole role = new AppUserRole();
        String anyString = anyString();
        user.setUserCode(anyString);
        user.setRoles(new HashSet<>());

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.of(role));

        userService.addRoleToUser(anyString, anyString);

        verify(userRepo).save(user);
    }

    @Test
    void throwsRoleExistsExceptionWhenAddRoleToUser() {
        AppUser user = new AppUser();
        AppUserRole role = new AppUserRole();
        String anyString = anyString();
        role.setName(anyString);
        user.setUserCode(anyString);
        user.setRoles(new HashSet<>());
        assert user.getRoles() != null;
        user.getRoles().add(role);

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.of(role));

        RoleExistsException exception =
                assertThrows(RoleExistsException.class, () -> userService.addRoleToUser(anyString, anyString));

        assertEquals(exception.getMessage(), String.format(ROLE_ALREADY_EXISTS, anyString));
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenAddRoleToUser() {
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.empty());

        UserCodeNotFoundException exception =
                assertThrows(UserCodeNotFoundException.class, () -> userService.addRoleToUser(anyString, anyString));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, anyString));
    }

    @Test
    void throwsRoleNotFoundExceptionWhenAddRoleToUser() {
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(new AppUser()));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.empty());

        RoleNotFoundException exception =
                assertThrows(RoleNotFoundException.class, () -> userService.addRoleToUser(anyString, anyString));

        assertEquals(exception.getMessage(), String.format(ROLE_NOT_FOUND, anyString));
    }

    @Test
    void canDeleteRoleFromUser() {
        AppUser user = new AppUser();
        AppUserRole role = new AppUserRole();
        String anyString = anyString();
        role.setName(anyString);
        user.setUserCode(anyString);
        user.setRoles(new HashSet<>());
        assert user.getRoles() != null;
        user.getRoles().add(role);

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.of(role));

        userService.deleteRoleFromUser(anyString, anyString);
    }

    @Test
    void throwsRoleNotFoundWhenDeleteRoleFromUser() {
        AppUser user = new AppUser();
        AppUserRole role = new AppUserRole();
        String anyString = anyString();
        role.setName(anyString);
        user.setUserCode(anyString);
        user.setRoles(new HashSet<>());

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(user));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.of(role));

        RoleNotFoundException exception =
                assertThrows(RoleNotFoundException.class, () -> userService.deleteRoleFromUser(anyString, anyString));

        assertEquals(exception.getMessage(), "User doesn't have: " + anyString);
    }

    @Test
    void throwsRoleNotFoundExceptionWhenDeleteRoleFromUser() {
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.of(new AppUser()));
        when(roleRepo.findAppUserRoleByName(anyString)).thenReturn(Optional.empty());

        RoleNotFoundException exception =
                assertThrows(RoleNotFoundException.class, () -> userService.deleteRoleFromUser(anyString, anyString));

        assertEquals(exception.getMessage(), String.format(ROLE_NOT_FOUND, anyString));
    }

    @Test
    void throwsUserCodeNotFoundExceptionWhenDeleteRoleFromUser() {
        String anyString = anyString();

        when(userRepo.findByUserCode(anyString)).thenReturn(Optional.empty());

        UserCodeNotFoundException exception =
                assertThrows(UserCodeNotFoundException.class, () -> userService.deleteRoleFromUser(anyString, anyString));

        assertEquals(exception.getMessage(), String.format(USER_CODE_NOT_FOUND, anyString));
    }

    @Test
    void canActivateUser() {
        AppUser user = new AppUser();
        userService.activateUser(user);

        verify(userRepo).save(user);
    }


}
