package com.example.utro.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
public class ProductDTO {
    private UUID article;
    @NotEmpty
    @Size(max = 30)
    private String name;
    @NotNull
    @Min(value = 100L, message = "Ширина должна быть больше 100 мм")
    private Double width;
    @NotNull
    @Min(value = 100L, message = "Длина должна быть больше 100 мм")
    private Double length;
    private String comment;
}