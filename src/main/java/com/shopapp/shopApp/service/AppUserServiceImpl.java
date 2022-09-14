package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import com.shopapp.shopApp.security.CustomPasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.shopapp.shopApp.mapper.AppUserMapper.mapToAppUser;

@Service
@AllArgsConstructor
@Transactional
public class AppUserServiceImpl implements UserDetailsService, AppUserService {

    private final AppUserRepository userRepository;
    private final AppUserRoleRepository roleRepository;
    private final CustomPasswordEncoder passwordEncoder;

    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    public AppUser getUserWithEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with email: " + email));
    }

    public AppUser getUserWithUserCode(String userCode) {
        return userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with userCode: " + userCode));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with email: " + email));
    }

    @Override
    public void saveUser(AppUserSaveUpdateDto user) {
        String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("This user already exists");
        }
        AppUser newUser = mapToAppUser(null, user);
        newUser.setPassword(passwordEncoder.passwordEncoder().encode(newUser.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void deleteUserWithUserCode(String userCode) {
        AppUser user = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with userCode: " + userCode));
        userRepository.delete(user);
    }

    @Override
    public void updateUser(String userCode, AppUserSaveUpdateDto user) {
        AppUser foundUser = getUserWithUserCode(userCode);
        foundUser.setName(user.getName());
        foundUser.setLastName(user.getLastName());
        foundUser.setEmail(user.getEmail());
        foundUser.setPassword(passwordEncoder.passwordEncoder().encode(user.getPassword()));
        foundUser.setPhoneNumber(user.getPhoneNumber());
        foundUser.setAddress(user.getAddress());

        userRepository.save(foundUser);
    }

    //TODO: ogarnąć czy nie lepszym pomysłem nie byłoby usunięcie fileda roles i doawanie bezpośrednio o authorities
    @Override
    public void addRoleToUser(String userCode, String roleName) {
        AppUser appUser = userRepository.findByUserCode(userCode)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with userCode: " + userCode));
        AppUserRole role = roleRepository.findAppUserRoleByName(roleName).orElseThrow(() -> new IllegalStateException("No role with name: " + roleName));
        appUser.getRoles().add(role);
        userRepository.save(appUser);
    }
}
