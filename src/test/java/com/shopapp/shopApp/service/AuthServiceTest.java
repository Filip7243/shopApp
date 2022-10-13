package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import com.shopapp.shopApp.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AppUserRepository userRepo;
    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private Authentication auth;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;

    @Test
    void canSignInUser() {//todo;
        String anyString = anyString();
        AppUser user = new AppUser();
        // to check if user exists spring uses override method loadByUsername() and that method uses findByEmail() from repo
        // so when authenticationManager.authenticate() in authService is called, it calls loadByUsername()
        when(userRepo.findByEmail(anyString)).thenReturn(Optional.of(user));

        this.auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(anyString, anyString));
        authService.signInUser(new LoginRequest());
    }


}
