package com.example.utro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class FurnitureProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Product product;
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private Furniture furniture;
    private String placement;
    private Double width;
    private Double length;
    private int turn;
    private int amount;
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private User user;
}