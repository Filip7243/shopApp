package com.shopapp.shopApp.service.appuser;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;

public interface AppUserService {

    AppUser createUser(AppUserSaveUpdateDto user);

    void deleteUserWithUserCode(String userCode);

    void updateUser(String userCode, AppUserSaveUpdateDto user);

    void addRoleToUser(String userCode, String roleName);

    void deleteRoleFromUser(String userCode, String roleName);


}
