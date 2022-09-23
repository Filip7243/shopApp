package com.shopapp.shopApp.controller;

import com.shopapp.shopApp.constants.ResponseConstants;
import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.exception.token.ConfirmationTokenConfirmedException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenExpiredException;
import com.shopapp.shopApp.exception.token.ConfirmationTokenNotFoundException;
import com.shopapp.shopApp.exception.user.BadEmailException;
import com.shopapp.shopApp.exception.user.UserExistsException;
import com.shopapp.shopApp.model.ConfirmationToken;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.service.auth.AuthServiceImpl;
import com.shopapp.shopApp.service.confirmationtoken.ConfirmationTokenServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.shopapp.shopApp.constants.ResponseConstants.EMAIL_CONFIRMED;
import static com.shopapp.shopApp.constants.ResponseConstants.USER_REGISTERED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;
    private final ConfirmationTokenServiceImpl tokenService;

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
        ConfirmationToken foundToken = tokenService.getToken(token);
        tokenService.confirmEmail(foundToken);
        return ResponseEntity.ok(EMAIL_CONFIRMED);
    }

}
