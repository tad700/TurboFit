package com._3.TurboFit.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
