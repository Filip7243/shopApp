package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.constants.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = EMAIL_REQUIRED)
    private String email;
    @NotBlank(message = PASSWORD_REQUIRED)
    private String password;
}
