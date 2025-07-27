package com._3.TurboFit.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {
    private Long carId;
    private String carName;
    private int horsePower;
    private String imageUrl;
}