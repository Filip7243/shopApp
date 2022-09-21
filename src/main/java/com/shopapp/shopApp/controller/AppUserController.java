package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserDisplayDto;
import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.exception.role.RoleNotFoundException;
import com.shopapp.shopApp.exception.user.UserCodeNotFoundException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.exception.user.UserNotFoundException;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.appuser.AppUserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.shopapp.shopApp.mapper.AppUserMapper.mapToAppUserDisplayDto;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserServiceImpl userService;
    private final JwtUtils jwtUtils;
    private final Environment env;

    @GetMapping("/all")
    public ResponseEntity<List<AppUserDisplayDto>> getUsers() {
        List<AppUserDisplayDto> users = mapToAppUserDisplayDto(userService.getUsers());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody AppUserSaveUpdateDto user) throws UserExistsException {
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("USER CREATED");
    }

    @DeleteMapping("/delete/{userCode}")
    public ResponseEntity<?> deleteUserWithUserCode(@PathVariable String userCode) throws UserCodeNotFoundException {
        userService.deleteUserWithUserCode(userCode);
        return ResponseEntity.status(HttpStatus.OK).body("DELETED");
    }

    @PutMapping("/update/{userCode}")
    public ResponseEntity<?> updateUser(@PathVariable String userCode,
                                        @RequestBody AppUserSaveUpdateDto user) throws UserCodeNotFoundException {
        userService.updateUser(userCode, user);
        return ResponseEntity.ok("UPDATED");
    }

    @PostMapping("/roles/add")
    public ResponseEntity<?> addRoleToUser(@RequestParam String userCode, @RequestParam String roleName)
            throws UserCodeNotFoundException, RoleNotFoundException {
        userService.addRoleToUser(userCode, roleName);
        return ResponseEntity.ok("ADDED ROLE TO USER");
    }

    @GetMapping("/accessToken/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws UserNotFoundException {
        String refreshToken = jwtUtils.getTokenFromHeader(request);
        if (refreshToken != null) {
            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            AppUser user = (AppUser) userService.loadUserByUsername(username);

            return ResponseEntity.ok(jwtUtils.refreshAccessToken(refreshToken, user));
        }
        return ResponseEntity.badRequest().body("Could not refresh the token!");
    }

}
