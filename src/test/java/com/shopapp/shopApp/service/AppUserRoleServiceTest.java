package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import com.shopapp.shopApp.service.appuser.AppUserRoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.ROLE_ALREADY_EXISTS;
import static com.shopapp.shopApp.constants.ExceptionsConstants.ROLE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppUserRoleServiceTest {

    @Mock
    private AppUserRoleRepository userRoleRepo;

    private AppUserRoleServiceImpl userRoleService;

    @BeforeEach
    void setUp() {
        this.userRoleService = new AppUserRoleServiceImpl(userRoleRepo);
    }

    @Test
    void canGetAllRoles() {
        userRoleService.getRoles();

        verify(userRoleRepo).findAll();
    }

    @Test
    void canSaveRole() {
        var role = new AppUserRole(null, "ROLE_USER", "USER");
        userRoleService.saveRole(role);

        verify(userRoleRepo).save(role);
    }

    @Test
    void throwsRoleExistsExceptionWhenSaveRole() {
        var role = new AppUserRole(null, "ROLE_USER", "USER");
        when(userRoleRepo.existsByName(role.getName())).thenReturn(true);

        RoleExistsException exception = assertThrows(RoleExistsException.class, () -> userRoleService.saveRole(role));

        assertEquals(String.format(ROLE_ALREADY_EXISTS, role.getName()), exception.getMessage());
    }

    @Test
    void canDeleteRoleWithName() {
        var role = new AppUserRole(null, "ROLE_USER", "USER");

        when(userRoleRepo.findAppUserRoleByName(role.getName())).thenReturn(Optional.of(role));
        userRoleService.deleteRoleWithName(role.getName());
        verify(userRoleRepo).delete(role);
    }

    @Test
    void throwsRoleNotFoundWhenDeleteRoleWithName() {
        var name = anyString();

        when(userRoleRepo.findAppUserRoleByName(name)).thenReturn(Optional.empty());
        RoleNotFoundException exception =
                assertThrows(RoleNotFoundException.class, () -> userRoleService.deleteRoleWithName(name));
        assertEquals(String.format(ROLE_NOT_FOUND, name), exception.getMessage());
    }

    @Test
    void canUpdateRole() {
        var role = new AppUserRole(null, "ROLE_USER", "USER");

        when(userRoleRepo.findAppUserRoleByName(role.getName())).thenReturn(Optional.of(role));
        userRoleService.updateRole(role.getName(), new AppUserRoleSaveUpdateDto());

        verify(userRoleRepo).save(role);
    }

    @Test
    void throwsRoleNotFoundExceptionWhenUpdateRole() {
        var roleName = anyString();

        var exception = assertThrows(RoleNotFoundException.class,
                () -> userRoleService.updateRole(roleName, new AppUserRoleSaveUpdateDto()));
        assertEquals(String.format(ROLE_NOT_FOUND, roleName), exception.getMessage());
    }


}
