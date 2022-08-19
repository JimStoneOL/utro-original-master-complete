package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class FurnitureWarehouseDTO {
    private Long id;
    @NotNull
    private UUID furnitureId;
    @NotNull
    @Min(value = 0L, message = "Количество должно быть положительным")
    private int amount;
}