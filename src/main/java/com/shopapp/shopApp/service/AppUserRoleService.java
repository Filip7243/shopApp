package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserRoleUpdateDto;
import com.shopapp.shopApp.model.AppUserRole;

public interface AppUserRoleService {

    void saveRole(AppUserRole role);
    void deleteRoleWithName(String name);
    void updateRole(String roleName, AppUserRoleUpdateDto role);

}
