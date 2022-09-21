package com.shopapp.shopApp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String reviewCode;
    private String topic;
    private String description;
    private Integer stars; // from 1 to 5
    @ManyToOne
    private Product product;
    @ManyToOne
    private AppUser user;
}
