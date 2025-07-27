package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/cars")

public class CarController {
    CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }


    @GetMapping
    public ResponseEntity<List<CarDTO>> getAll(){
        return new ResponseEntity<>(carService.getAll(),HttpStatus.OK);
    }

    @PutMapping("/update-horsepower/{userId}")
    public ResponseEntity<CarDTO> updateHorsePower(@PathVariable Long userId) {
        CarDTO updatedCar = carService.updateCarHorsePower(userId);
        return ResponseEntity.ok(updatedCar);
    }
}
