package com.example.utro.dto;

import com.example.utro.annotations.ValidUUID;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ClothProductDTO {
    private Long id;
    @NotNull
    private UUID clothId;
    @NotNull
    private UUID productId;
}