package com.shopapp.shopApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore // i don't want to display info about user from shopping cart
    @OneToOne(fetch = EAGER, targetEntity = AppUser.class)
    private AppUser user;
    @OneToMany(fetch = LAZY)
    private Set<CartItem> items;
    private LocalDateTime createdAt;
    private Double totalPrice;
}
