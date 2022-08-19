package com.example.utro.payload.request;

import com.example.utro.entity.enums.ERole;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GiveRoleRequest {
    @NotNull
    private ERole role;
    @NotNull
    private Long userId;
}
