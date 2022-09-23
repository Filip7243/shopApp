package com.shopapp.shopApp.dto;

import com.shopapp.shopApp.constants.ValidationConstants;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.shopapp.shopApp.constants.ValidationConstants.*;

@Getter
@Setter
@AllArgsConstructor
public class ProductReviewAddUpdateDto {

    @NotBlank(message = TOPIC_REQUIRED)
    @Size(max = 35, message = "Topic is too long. Max length is 35 characters")
    private String topic;
    @NotBlank(message = DESCRIPTION_REQUIRED)
    @Size(max = 255, message = VALID_DESCRIPTION_MAX_LENGTH)
    private String description;
    @Min(value = 1, message = VALID_MIN_VALUE)
    @Max(value = 5, message = VALID_MAX_VALUE)
    private Integer stars; // from 1 to 5
}
