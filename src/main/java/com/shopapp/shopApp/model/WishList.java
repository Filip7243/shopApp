package com.shopapp.shopApp.model;

import lombok.*;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.FetchType.EAGER;

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
    @OneToMany(fetch = EAGER)
    private Set<Product> wishListItems;
    @OneToOne
    private AppUser user;
}
