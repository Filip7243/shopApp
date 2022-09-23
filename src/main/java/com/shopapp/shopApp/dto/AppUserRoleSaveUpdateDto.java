package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.constants.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserRoleSaveUpdateDto {

    @NotBlank(message = NAME_REQUIRED)
    @Size(max = 35, message = VALID_NAME_MAX_LENGTH)
    private String name; // unique
    @NotBlank(message = DESCRIPTION_REQUIRED)
    @Size(max = 255, message = VALID_DESCRIPTION_MAX_LENGTH)
    private String description;
}
