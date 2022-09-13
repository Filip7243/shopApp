package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;

public interface AppUserService {

    void saveUser(AppUserSaveUpdateDto user);
    void deleteUserWithUserCode(String userCode);
    void updateUser(String userCode, AppUserSaveUpdateDto user);
    void addRoleToUser(String userCode, String roleName);


}
