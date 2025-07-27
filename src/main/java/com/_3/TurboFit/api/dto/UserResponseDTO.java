package com._3.TurboFit.api.dto;

import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.UserCar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String username;
    private String email;
    private String phone;
    private String role;
    private UserCar car;
    private int totalWorkouts;
    private int points;


}
