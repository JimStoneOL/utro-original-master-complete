package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class FurnitureProductDTO {
    private Long id;
    @NotNull
    private UUID productId;
    @NotNull
    private UUID furnitureId;
    @Size(min = 5,max = 70)
    private String placement;
    @Min(value = 0L, message = "Ширина должна быть больше положительной")
    private Double width;
    @Min(value = 0L, message = "Длина должна быть положительной")
    private Double length;
    private int turn;
    @NotNull
    @Min(value = 1L, message = "Количество должно быть больше 0")
    private int amount;
}