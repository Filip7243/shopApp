package com.shopapp.shopApp.service.appuser;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.model.AppUserRole;

public interface AppUserRoleService {

    void saveRole(AppUserRole role);

    void deleteRoleWithName(String name);

    void updateRole(String roleName, AppUserRoleSaveUpdateDto role);

}
