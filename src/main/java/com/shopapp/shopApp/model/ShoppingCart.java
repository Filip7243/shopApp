package com.shopapp.shopApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private String shoppingCartCode;
    @JsonIgnore // i don't want to display info about user from shopping cart
    @ManyToOne(fetch = LAZY)
    private AppUser user;
    @OneToMany(fetch = LAZY)
    @JoinColumn(name = "cartId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<CartItem> items;
    private LocalDateTime createdAt;
    private Double totalPrice;
}
