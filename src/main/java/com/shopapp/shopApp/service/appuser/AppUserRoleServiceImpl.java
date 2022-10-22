package com.shopapp.shopApp.service.appuser;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.shopapp.shopApp.constants.ExceptionsConstants.ROLE_ALREADY_EXISTS;
import static com.shopapp.shopApp.constants.ExceptionsConstants.ROLE_NOT_FOUND;

@Service
@AllArgsConstructor
public class AppUserRoleServiceImpl implements AppUserRoleService {

    private final AppUserRoleRepository roleRepository;

    public List<AppUserRole> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(AppUserRole role) {
        String name = role.getName();
        if (roleRepository.existsByName(name)) {
            throw new RoleExistsException(String.format(ROLE_ALREADY_EXISTS, role.getName()));
        }
        roleRepository.save(role);
    }

    @Override
    public void deleteRoleWithName(String roleName) {
        AppUserRole role = roleRepository.findAppUserRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(String.format(ROLE_NOT_FOUND, roleName)));
        roleRepository.delete(role);
    }

    @Override
    public void updateRole(String roleName, AppUserRoleSaveUpdateDto role) {
        AppUserRole foundRole = roleRepository.findAppUserRoleByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(String.format(ROLE_NOT_FOUND, roleName)));
        foundRole.setName(role.getName());
        foundRole.setDescription(role.getDescription());

        roleRepository.save(foundRole);
    }
}
