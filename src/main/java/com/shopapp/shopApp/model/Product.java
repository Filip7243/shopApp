package com.shopapp.shopApp.model;

import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
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
    @ManyToMany(fetch = LAZY)
    @BatchSize(size = 10) //
    private List<Category> categories;
    @OneToMany
    @JoinColumn(name = "productId")
    private List<ProductReview> reviews;

}
