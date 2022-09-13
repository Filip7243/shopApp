package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.service.AppUserRoleServiceImpl;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserServiceImpl userService;
    @GetMapping("/all")
    public List<AppUser> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/save")
    public void saveUser(AppUserSaveUpdateDto user) {
        userService.saveUser(user);
    }

    @DeleteMapping("/delete/{userCode}")
    public void deleteUserWithUserCode(@PathVariable String userCode) {
        userService.deleteUserWithUserCode(userCode);
    }

    @PutMapping("/update/{userCode}")
    public void updateUser(@PathVariable String userCode, AppUserSaveUpdateDto user) {
        userService.updateUser(userCode, user);
    }

    @PostMapping("/roles/add")
    public void addRoleToUser(String email, String roleName) {
        userService.addRoleToUser(email, roleName);
    }


}
