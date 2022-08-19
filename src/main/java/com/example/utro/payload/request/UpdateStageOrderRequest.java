package com.example.utro.payload.request;

import com.example.utro.entity.enums.EStage;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UpdateStageOrderRequest {
    @NotNull
    private UUID orderId;
    @NotEmpty
    private EStage stage;
}
