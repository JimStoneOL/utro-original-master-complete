package com.example.utro.entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Furniture {
    @Id
    private UUID article=UUID.randomUUID();
    private String name;
    private String type;
    private Double width;
    private Double length;
    private int weight;
    @Column(columnDefinition = "decimal(10,2)")
    private double price;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},
            mappedBy = "furniture")
    private List<FurnitureWarehouse> furnitureWarehouses;
}