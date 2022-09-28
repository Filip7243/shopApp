package com.shopapp.shopApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String wishListCode;
    @OneToMany(fetch = LAZY)
    private Set<Product> wishListItems;
    @JsonIgnore
    @OneToOne(fetch = LAZY)
    @JoinColumn
    private AppUser user;
}
