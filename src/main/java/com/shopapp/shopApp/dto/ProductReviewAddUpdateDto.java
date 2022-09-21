package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductReviewAddUpdateDto {

    private String topic;
    private String description;
    private Integer stars; // from 1 to 5
}
