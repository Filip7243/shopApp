package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveUpdateDto {

    @NotBlank(message = NAME_REQUIRED)
    @Size(max = 35, message = VALID_NAME_MAX_LENGTH)
    private String name;
    @NotBlank(message = DESCRIPTION_REQUIRED)
    @Size(max = 255, message = VALID_DESCRIPTION_MAX_LENGTH)
    private String description;
    @Min(value = 1, message = VALID_MIN_VALUE)
    @Max(value = 999999999, message = "Price to high!")
    private Double price;
    @Min(value = 1, message = VALID_MIN_VALUE)
    private Integer inStock;
    private String imageUrl;
    // TODO: add Category field for ui
}
