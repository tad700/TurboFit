package com._3.TurboFit.api.dto;

import com._3.TurboFit.api.models.Car;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Data
public class UserDTO {
    //@JsonIgnore
    private long userId;
    private String username;
    private String email;
    private String password;
    private String phone;
    private int totalWorkouts;
    private int points;
    private CarDTO car;
    private String role;
}
