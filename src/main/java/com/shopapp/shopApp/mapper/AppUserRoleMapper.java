package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.AppUserRoleSaveUpdateDto;
import com.shopapp.shopApp.model.AppUserRole;

public class AppUserRoleMapper {

    public static AppUserRole mapToAppUserRole(AppUserRoleSaveUpdateDto role) {
        return AppUserRole.builder()
                .id(null)
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }
}
