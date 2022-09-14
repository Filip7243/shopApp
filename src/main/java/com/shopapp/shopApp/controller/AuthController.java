package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.service.AuthServiceImpl;
import com.shopapp.shopApp.service.ConfirmationTokenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final ConfirmationTokenServiceImpl tokenService;

    @PostMapping("/signIn")
    public JwtResponse signIn(@RequestBody LoginRequest loginRequest) {
        return authService.signInUser(loginRequest);
    }

    @PostMapping("/signUp")
    public AppUser singUp(@RequestBody AppUserSaveUpdateDto registerRequest) {
        return authService.signUpUser(registerRequest);
    }
    @GetMapping("/confirm")
    public void confirmToken(@RequestParam("token") String token) {
        ConfirmationToken foundToken = tokenService.getToken(token);
        tokenService.confirmEmail(foundToken);
    }
}
