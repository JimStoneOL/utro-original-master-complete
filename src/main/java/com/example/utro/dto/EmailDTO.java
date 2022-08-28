package com.example.utro.dto;

import com.example.utro.entity.enums.EmailStatus;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmailDTO {
    private Long id;
    @NotEmpty
    private String whomUserEmail;
    private String fromUserEmail;
    @NotNull
    private EmailStatus status;
    @NotEmpty
    @Size(max = 30)
    private String heading;
    @NotEmpty
    private String message;
}
