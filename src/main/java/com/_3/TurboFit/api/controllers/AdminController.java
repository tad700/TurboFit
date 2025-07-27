package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.Exercise;
import com._3.TurboFit.api.service.CarService;
import com._3.TurboFit.api.service.ExerciseService;
import com._3.TurboFit.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")

public class AdminController {
CarService carService;
ExerciseService exerciseService;
UserService userService;

    public AdminController(CarService carService, ExerciseService exerciseService, UserService userService) {
        this.carService = carService;
        this.exerciseService = exerciseService;
        this.userService = userService;
    }

    @PostMapping("/addCar/")
    public ResponseEntity<CarDTO> addCar(@RequestBody CarDTO carDTO){
        return new ResponseEntity<>(carService.createCar(carDTO), HttpStatus.CREATED);
    }
    @PostMapping("/createExercise/")
    public ResponseEntity<ExerciseResponseDTO> createExercise(@RequestBody ExerciseCreateDTO exerciseCreateDTO){
        return new ResponseEntity<>(exerciseService.createExercise(exerciseCreateDTO), HttpStatus.CREATED);

    }
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id){
        return new ResponseEntity<>(userService.deleteUser(id),HttpStatus.OK);
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);

    }
    @DeleteMapping("delete/user/{username}")
    public ResponseEntity<UserResponseDTO> deleteUserByUsername(@PathVariable String username){
        return new ResponseEntity<>(userService.deleteUserByUsername(username),HttpStatus.OK);
    }
    @DeleteMapping("delete/car/{carName}")
    public ResponseEntity<CarDTO> deleteCarByName(@PathVariable String carName){
        return new ResponseEntity<>(carService.deleteCarByName(carName),HttpStatus.OK);
    }

    @DeleteMapping("/delete/exercise/{name}")
    public ResponseEntity<ExerciseResponseDTO> deleteUser(@PathVariable String name){
        return new ResponseEntity<>(exerciseService.deleteExerciseByName(name),HttpStatus.OK);
    }
}
