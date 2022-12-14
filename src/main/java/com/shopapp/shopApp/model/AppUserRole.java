package com.shopapp.shopApp.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name; // unique
    private String description;
}