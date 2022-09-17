package com.shopapp.shopApp.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Double price;
    private Integer inStock;
    private String imageUrl;
    @ManyToOne(fetch = LAZY)
    private Category category;
}
