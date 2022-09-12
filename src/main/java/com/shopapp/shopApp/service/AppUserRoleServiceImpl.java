package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserRoleServiceImpl implements AppUserRoleService {

    private final AppUserRoleRepository roleRepository;

    @Override
    public void saveRole(AppUserRole role) {
        String name = role.getName();
        if (roleRepository.existsByName(name)) {
            throw new IllegalStateException("There is a role with this name already");
        }
        roleRepository.save(role);
    }

    @Override
    public void deleteRoleWithName(String name) {
        AppUserRole role = roleRepository.findAppUserRoleByName(name)
                .orElseThrow(() -> new IllegalStateException("There is no role with name"));
        roleRepository.delete(role);
    }

    @Override
    public void updateRole(AppUserRole role) {
        String name = role.getName();
        if(roleRepository.existsByName(name)) {
            roleRepository.save(role);
        } else {
            throw new IllegalStateException("There is no role with name: " + name);
        }

    }
}
