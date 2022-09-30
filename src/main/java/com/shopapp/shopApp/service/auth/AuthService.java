package com.shopapp.shopApp.service.auth;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.security.jwt.JwtResponse;

public interface AuthService {

    JwtResponse signInUser(LoginRequest loginRequest);

    void signUpUser(AppUserSaveUpdateDto registerRequest);

    void forgetPassword(String email);

}
