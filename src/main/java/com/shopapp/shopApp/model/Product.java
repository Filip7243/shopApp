package com.shopapp.shopApp.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productCode;
    private String name;
    private String description;
    private Double price;
    private Integer inStock;
    private String imageUrl;
    @ManyToMany(fetch = LAZY)
    @BatchSize(size = 10)
    private List<Category> categories;
    @OneToMany
    @JoinColumn(name = "productId")
    @BatchSize(size = 50)
    private List<ProductReview> reviews;

}
