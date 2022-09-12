package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveDto;
import com.shopapp.shopApp.model.AppUser;

public interface AppUserService {

    void saveUser(AppUserSaveDto user);
    void deleteUserWithUserCode(String userCode);
    void updateUser(AppUser user);
    void addRoleToUser(String email, String roleName);


}
