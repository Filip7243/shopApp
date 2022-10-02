package com.shopapp.shopApp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static com.shopapp.shopApp.constants.ExceptionsConstants.BAD_EMAIL;
import static com.shopapp.shopApp.constants.ValidationConstants.EMAIL_REGEX;
import static com.shopapp.shopApp.constants.ValidationConstants.EMAIL_REQUIRED;

@Getter
@Setter
public class EmailRequest {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(regexp = EMAIL_REGEX, message = BAD_EMAIL)
    private String email;
}
