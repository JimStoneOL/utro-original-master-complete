package com.example.utro.payload.request;

import com.example.utro.entity.enums.EStage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class CustomerOrderRequest {
    private UUID id;
    private List<Long> orderedProducts;
}
