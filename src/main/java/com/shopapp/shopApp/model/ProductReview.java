package com.shopapp.shopApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
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
    private Long productId;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser user;
}
