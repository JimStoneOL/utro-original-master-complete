package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
public class ClothDTO {
    private UUID article;
    @NotEmpty
    @Size(max = 30)
    private String name;
    @Size(max = 40)
    private String color;
    @Size(max = 50)
    private String drawing;
    @NotEmpty
    @Size(max = 80)
    private String structure;
    @NotNull
    @Min(value = 100L, message = "Ширина должна быть больше 100 мм")
    private Double width;
    @NotNull
    @Min(value = 100L, message = "Длина должна быть больше 100 мм")
    private Double length;
    @NotNull
    @Min(value = 0L, message = "Цена не может быть отрицательной")
    private Double price;
    private List<Long> clothWarehousesId;
}