package com.shopapp.shopApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserSaveDto {

    private String name;
    private String lastName;
    private String email; // username
    private byte[] password;
}
