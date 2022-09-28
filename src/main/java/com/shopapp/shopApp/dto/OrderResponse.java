package com.shopapp.shopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String email;
    private List<CartItemDto> items;
    private Double totalPrice;
}
