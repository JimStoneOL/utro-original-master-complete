package com.example.utro.dto;

import com.example.utro.entity.enums.EStage;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    private EStage stage;
    private Double price;
    private List<Long> orderedProducts;
    private List<String> username;
}