package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserDisplayDto;
import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleExistsException;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.*;
import static com.shopapp.shopApp.mapper.AppUserMapper.mapToAppUserDisplayDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserServiceImpl userService;
    private final JwtUtils jwtUtils;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserDisplayDto>> getUsers() {
        List<AppUserDisplayDto> users = mapToAppUserDisplayDto(userService.getUsers());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/create")
    public ResponseEntity<?> saveUser(@RequestBody @Valid AppUserSaveUpdateDto user) throws UserExistsException {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format(USER_CREATED, user.getEmail()));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUserWithUserCode(@RequestParam String userCode) throws UserCodeNotFoundException {
        userService.deleteUserWithUserCode(userCode);
        return ResponseEntity.status(HttpStatus.OK).body(String.format(USER_DELETED, userCode));
    }

    @PutMapping("/update/{userCode}")
    public ResponseEntity<?> updateUser(@PathVariable String userCode,
                                        @RequestBody @Valid AppUserSaveUpdateDto user) throws UserCodeNotFoundException {
        userService.updateUser(userCode, user);
        return ResponseEntity.ok(String.format(USER_UPDATED, userCode));
    }

    @PostMapping("/roles/add")
    public ResponseEntity<?> addRoleToUser(@RequestParam String userCode, @RequestParam String roleName)
            throws UserCodeNotFoundException, RoleNotFoundException, RoleExistsException {
        userService.addRoleToUser(userCode, roleName);
        return ResponseEntity.ok(String.format(ROLE_ADDED_TO_USER, roleName, userCode));
    }

    @DeleteMapping("roles/delete")
    public ResponseEntity<?> deleteRoleFromUser(@RequestParam String userCode, @RequestParam String roleName)
            throws UserCodeNotFoundException, RoleNotFoundException {
        userService.deleteRoleFromUser(userCode, roleName);
        return ResponseEntity.ok(String.format(ROLE_ADDED_TO_USER, roleName, userCode));
    }

    @GetMapping("/accessToken/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws UserNotFoundException {
        String refreshToken = jwtUtils.getTokenFromHeader(request);
        if (refreshToken != null) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            AppUser user = (AppUser) userService.loadUserByUsername(username);

            return ResponseEntity.ok(jwtUtils.refreshAccessToken(refreshToken, user));
        }
        return ResponseEntity.badRequest().body(String.format(ACCESS_TOKEN_NOT_REFRESHED));
    }

    @GetMapping("/account/enable")
    public ResponseEntity<?> enableAccount(@RequestParam String userCode) throws UserCodeNotFoundException{
        AppUser user = userService.getUserWithUserCode(userCode);
        userService.activateUser(user);

        return ResponseEntity.ok().body("User activated");
    }

}
