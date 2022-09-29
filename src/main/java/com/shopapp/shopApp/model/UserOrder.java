package com.shopapp.shopApp.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderCode;
//    private String cartCode;
    @OneToMany(fetch = FetchType.LAZY)
    private List<CartItem> orderedItems;
    @OneToOne(fetch = FetchType.LAZY)
    private ShoppingCart cart;
    private LocalDateTime orderedAt;
    private Boolean hasPaid;
    private Double totalPrice;
}
