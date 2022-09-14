package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserSaveUpdateDto {

    private String name;
    private String lastName;
    private String email; // username
    private String password;
    private String phoneNumber;
    private String address;
}
