package com.shopapp.shopApp.service;

import com.shopapp.shopApp.model.AppUserRole;

public interface AppUserRoleService {

    void saveRole(AppUserRole role);
    void deleteRoleWithName(String name);
    void updateRole(AppUserRole role);

}
