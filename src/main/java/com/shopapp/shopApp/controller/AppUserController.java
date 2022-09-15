package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserDisplayDto;
import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.service.AppUserRoleServiceImpl;
import com.shopapp.shopApp.service.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.shopapp.shopApp.mapper.AppUserMapper.mapToAppUserDisplayDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserServiceImpl userService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserDisplayDto>> getUsers() {
        List<AppUserDisplayDto> users = mapToAppUserDisplayDto(userService.getUsers());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody AppUserSaveUpdateDto user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("USER CREATED");
        } catch (UserExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/delete/{userCode}")
    public ResponseEntity<?> deleteUserWithUserCode(@PathVariable String userCode) {
        try {
            userService.deleteUserWithUserCode(userCode);
            return ResponseEntity.status(HttpStatus.OK).body("DELETED");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{userCode}")
    public ResponseEntity<?> updateUser(@PathVariable String userCode,
                                        @RequestBody AppUserSaveUpdateDto user) {
        try {
            userService.updateUser(userCode, user);
            return ResponseEntity.ok("UPDATED");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/roles/add")
    public ResponseEntity<?> addRoleToUser(@RequestParam String userCode, @RequestParam String roleName) {
        try {
            userService.addRoleToUser(userCode, roleName);
            return ResponseEntity.ok("ADDED ROLE TO USER");
        } catch (UsernameNotFoundException | RoleNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
