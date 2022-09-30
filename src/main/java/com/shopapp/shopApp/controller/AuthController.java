package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.EmailRequest;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.dto.PasswordForgetRequest;
import com.shopapp.shopApp.exception.token.ConfirmationTokenConfirmedException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenExpiredException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.exception.user.BadEmailException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.model.PasswordResetToken;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.service.auth.AuthServiceImpl;
import com.shopapp.shopApp.service.confirmationtoken.ConfirmationTokenServiceImpl;
import com.shopapp.shopApp.service.resetpasswordtoken.PasswordResetTokenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.shopapp.shopApp.constants.ResponseConstants.EMAIL_CONFIRMED;
import static com.shopapp.shopApp.constants.ResponseConstants.USER_REGISTERED;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final PasswordResetTokenServiceImpl passwordResetTokenService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> signIn(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.signInUser(loginRequest));
    }

    @PostMapping("/signUp")
    public ResponseEntity<String> singUp(@RequestBody @Valid AppUserSaveUpdateDto registerRequest)
            throws UserExistsException, BadEmailException, IllegalStateException {
        authService.signUpUser(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format(USER_REGISTERED, registerRequest.getEmail()));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token)
            throws ConfirmationTokenNotFoundException, ConfirmationTokenExpiredException, ConfirmationTokenConfirmedException {
        ConfirmationToken foundToken = confirmationTokenService.getToken(token);
        confirmationTokenService.confirmEmail(foundToken);
        return ResponseEntity.ok(EMAIL_CONFIRMED);
    }
//todo; automatically delete expired things

    @GetMapping("/password/forget")
    public ResponseEntity<?> forgetPassword(@RequestBody EmailRequest email) {
        authService.forgetPassword(email.getEmail());
        return ResponseEntity.ok("Check your email: " + email.getEmail());
    }
    @GetMapping("/reset/password")
    public ResponseEntity<?> restartPassword(@RequestParam("token") String token) {
        System.out.println("ESSA");
        return null;
    }

}
