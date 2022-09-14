package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.service.AuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/signIn")
    public JwtResponse signIn(@RequestBody LoginRequest loginRequest) {
        return authService.signInUser(loginRequest);
    }

    @PostMapping("/signUp")
    public AppUser singUp(@RequestBody AppUserSaveUpdateDto registerRequest) {

        return null;
    }
}
