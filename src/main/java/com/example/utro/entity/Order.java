package com.example.utro.entity;

import com.example.utro.entity.enums.EStage;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    private UUID id;
    @Column(updatable = false)
    @JsonFormat(pattern = "dd-mm-yyyy ss:mm:HH")
    private LocalDateTime createdDate;
    private EStage stage;
    @ManyToMany(mappedBy = "orders")
    private List<User> user;
    @Column(columnDefinition = "decimal(10,2)")
    private double price;
    @OneToMany(mappedBy = "order",fetch = FetchType.LAZY)
    private List<OrderedProduct> orderedProducts;
    @PrePersist
    protected void onCreate()
    {
        this.createdDate = LocalDateTime.now();
    }
}