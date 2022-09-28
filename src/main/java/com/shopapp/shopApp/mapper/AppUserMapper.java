package com.shopapp.shopApp.mapper;

import com.shopapp.shopApp.dto.AppUserDisplayDto;
import com.shopapp.shopApp.dto.AppUserSaveUpdateDto;
import com.shopapp.shopApp.model.AppUser;
import com.shopapp.shopApp.model.AppUserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class AppUserMapper {

    public static AppUser mapToAppUser(Long id, AppUserSaveUpdateDto user) {
        return AppUser.builder()
                .id(id)
                .userCode(UUID.randomUUID().toString())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .roles(Set.of(new AppUserRole(null, "ROLE_SUPER_ADMIN", "User can READ")))
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusYears(1))
                .isExpired(false)
                .isLocked(false)
                .isCredentialsExpired(false)
                .isEnabled(true)
                .build();
    }

    public static List<AppUserDisplayDto> mapToAppUserDisplayDto(List<AppUser> users) {
        List<AppUserDisplayDto> displayDto = new ArrayList<>();
        users.stream().map(user -> displayDto.add(
                new AppUserDisplayDto(user.getName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getPhoneNumber(),
                        user.getAddress(),
                        user.getUserCode(),
                        user.getRoles())
        )).collect(Collectors.toList());
        return displayDto;
    }

}
