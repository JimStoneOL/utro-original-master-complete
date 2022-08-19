package com.example.utro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class ClothWarehouse {
    @Id
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Cloth cloth;
    private Double length;
}