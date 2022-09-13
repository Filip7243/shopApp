package com.shopapp.shopApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserRoleUpdateDto {

    private String name; // unique
    private String description;
}
