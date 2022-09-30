package com.shopapp.shopApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordForgetRequest {

    private String newPassword; //todo; validation
}
