package com.example.utro.payload.response;

import com.example.utro.entity.OrderedProduct;
import com.example.utro.entity.enums.EStage;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private EStage stage;
    private Double price;
    private List<OrderedProduct> orderedProducts;
    private String username;
}