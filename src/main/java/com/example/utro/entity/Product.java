package com.example.utro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Product {
    @Id
    private UUID article;
    @Column(unique = false)
    private String name;
    @Column
    private Double width;
    @Column
    private Double length;
    @Column(columnDefinition = "text")
    private String comment;
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private User user;
    @OneToMany(mappedBy = "product")
    private List<ClothProduct> clothProducts;
    @OneToMany(mappedBy = "product")
    private List<FurnitureProduct> furnitureProducts;
}