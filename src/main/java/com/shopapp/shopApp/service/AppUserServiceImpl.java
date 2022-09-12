package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements UserDetailsService, AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserRoleRepository roleRepository;

    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new UsernameNotFoundException("There is no user with email: " + email));
    }

    @Override
    public void saveUser(AppUserSaveDto user) {
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("This user already exists");
        }

        //TODO: make mapper
        AppUser newUser = new AppUser(
                null,
                UUID.randomUUID().toString(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                false,
                false,
                false,
                true
        );

        userRepository.save(newUser);
    }

    @Override
    public void deleteUserWithUserCode(String userCode) {
        AppUser user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with userCode: " + userCode));
        userRepository.delete(user);
    }

    @Override
    public void updateUser(AppUser user) {
        userRepository.save(user);
    }

    @Override
    public void addRoleToUser(String email, String roleName) {
        AppUser appUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with email: " + email));
        AppUserRole role = roleRepository.findAppUserRoleByName(roleName).orElseThrow(() -> new IllegalStateException("No role with name: " + roleName));
        SimpleGrantedAuthority a = new SimpleGrantedAuthority(roleName);
        appUser.getAuthorities().add(a);
    }
}
