package com.example.utro.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class ClothProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private Cloth cloth;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Product product;
    @ManyToOne(cascade = CascadeType.REFRESH,fetch = FetchType.LAZY)
    private User user;
}