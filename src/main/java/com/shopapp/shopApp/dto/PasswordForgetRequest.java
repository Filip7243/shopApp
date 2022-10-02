package com.shopapp.shopApp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
public class PasswordForgetRequest {

    @NotBlank(message = PASSWORD_REQUIRED)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_NOT_VALID)
    private String newPassword;
}
