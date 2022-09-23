package com.shopapp.shopApp.dto;

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
public class CategorySaveUpdateDto {

    @NotBlank(message = NAME_REQUIRED)
    @Size(max = 35, message = VALID_NAME_MAX_LENGTH)
    private String categoryName;
    @NotBlank(message = DESCRIPTION_REQUIRED)
    @Size(max = 255, message = VALID_DESCRIPTION_MAX_LENGTH)
    private String description;
    private String imageUrl;
}
