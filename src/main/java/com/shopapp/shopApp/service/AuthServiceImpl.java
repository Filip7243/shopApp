package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.dto.LoginRequest;
import com.shopapp.shopApp.mapper.AppUserMapper;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.security.jwt.JwtResponse;
import com.shopapp.shopApp.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final AppUserRepository userRepository;

    @Override
    public JwtResponse signInUser(LoginRequest loginRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = JwtUtils.generateJwtAccessToken(authentication);
        String refreshToken = JwtUtils.generateJwtRefreshToken(authentication);
        String username = JwtUtils.getUsernameFromJwtToken(accessToken);

        AppUser user = (AppUser) authentication.getPrincipal();
        List<String> authorities = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        System.out.println("accessToken " + accessToken);
        System.out.println("refreshToken " + refreshToken);

        return new JwtResponse(
                username,
                accessToken,
                refreshToken,
                user.getUserCode(),
                authorities
        );
    }

    @Override
    public AppUser signUpUser(AppUserSaveUpdateDto registerRequest) {
        // validate email

        AppUser newUser = AppUserMapper.mapToAppUser(null, registerRequest);

        //send email

        userRepository.save(newUser);
        return newUser;
    }
}
