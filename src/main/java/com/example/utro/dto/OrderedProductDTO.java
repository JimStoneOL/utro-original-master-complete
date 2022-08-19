package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;


@Data
public class OrderedProductDTO {
    private Long id;
    @NotNull
    private UUID productId;
    @NotNull
    private UUID orderId;
    @NotNull
    @Min(value = 1L, message = "Количество должно быть больше 0")
    private int amount;
}
