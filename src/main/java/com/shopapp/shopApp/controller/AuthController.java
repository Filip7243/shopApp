package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.service.AuthServiceImpl;
import com.shopapp.shopApp.service.ConfirmationTokenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final ConfirmationTokenServiceImpl tokenService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> signIn(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signInUser(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> singUp(@RequestBody AppUserSaveUpdateDto registerRequest) {
        try {
            authService.signUpUser(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("User with username" + registerRequest.getEmail() + " registered");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token) {
        try {
            ConfirmationToken foundToken = tokenService.getToken(token);
            tokenService.confirmEmail(foundToken);
            return ResponseEntity.ok("CONFIRMED!");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.GONE).body(e.getMessage());
        }
    }
}
