package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ClothWarehouseDTO {
    private Long id;
    @NotNull
    private UUID clothId;
    @NotNull
    @Min(value = 0L, message = "Длина должна быть положительной")
    private Double length;
}