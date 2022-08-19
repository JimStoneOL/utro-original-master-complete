package com.example.utro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Cloth {
    @Id
    private UUID article;
    private String name;
    private String color;
    private String drawing;
    private String structure;
    private Double width;
    private Double length;
    @Column(columnDefinition = "decimal(10,2)")
    private double price;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "cloth")
    private List<ClothWarehouse> clothWarehouses;
}