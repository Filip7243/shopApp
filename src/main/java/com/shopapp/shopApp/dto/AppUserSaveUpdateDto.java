package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.shopapp.shopApp.constants.ExceptionsConstants.BAD_EMAIL;
import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserSaveUpdateDto {

    @NotBlank(message = NAME_REQUIRED)
    @Size(max = 35, message = VALID_NAME_MAX_LENGTH)
    private String name;
    @NotBlank(message = LAST_NAME_REQUIRED)
    @Size(max = 60, message = "Last name too long! Max length is 60 characters.")
    private String lastName;
    @NotBlank(message = EMAIL_REQUIRED)
    @Email(regexp = EMAIL_REGEX, message = BAD_EMAIL)
    private String email; // username
    @NotBlank(message = PASSWORD_REQUIRED)
    @Pattern(regexp = PASSWORD_REGEX, message = PASSWORD_NOT_VALID)
    private String password;
    @NotBlank(message = PHONE_NUMBER_REQUIRED)
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = "Phone number not valid!")
    private String phoneNumber;
    @NotBlank(message = ADDRESS_REQUIRED)
    private String address;
}
