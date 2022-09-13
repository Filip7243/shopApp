package com.shopapp.shopApp.service;

import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;
import com.shopapp.shopApp.repository.AppUserRepository;
import com.shopapp.shopApp.repository.AppUserRoleRepository;
import lombok.AllArgsConstructor;
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

        //TODO: make mapper
        AppUser newUser = new AppUser(
                null,
                UUID.randomUUID().toString(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                "123-456-789",
                "New York",
                Set.of(new AppUserRole(null, "ROLE_USER", "User can READ")),
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
    public void updateUser(String userCode, AppUserSaveUpdateDto user) {
        AppUser foundUser = getUserWithUserCode(userCode);
        foundUser.setName(user.getName());
        foundUser.setLastName(user.getLastName());
        foundUser.setEmail(user.getEmail());
        foundUser.setPassword(user.getPassword());
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
