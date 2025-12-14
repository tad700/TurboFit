package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.*;
import com._3.TurboFit.api.models.UserCar;
import com._3.TurboFit.api.models.Workout;
import com._3.TurboFit.api.service.ExerciseService;
import com._3.TurboFit.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController {
    private UserService userService;
    private ExerciseService exerciseService;

    public UserController(UserService userService, ExerciseService exerciseService) {
        this.userService = userService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("{userId}/car")
    public ResponseEntity<UserCar> getUserCar(@PathVariable Long userId) {
        UserCar car = userService.getUserCar(userId);
        return ResponseEntity.ok(car);
    }

    @PutMapping("{userId}/selectCar/{carId}")
    public ResponseEntity<UserResponseDTO> selectUserCar(
            @PathVariable Long userId,
            @PathVariable Long carId) {


        return new ResponseEntity<>( userService.selectCar(userId,carId),HttpStatus.OK);
    }
    @GetMapping("{userId}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long userId){
        return new ResponseEntity<>(userService.findById(userId),HttpStatus.OK);
    }
    @GetMapping("/userWorkouts/{userId}")
    public ResponseEntity<List<WorkoutDTO>> userWorkouts(@PathVariable Long userId){
        return new ResponseEntity<>(userService.getUserWorkouts(userId),HttpStatus.OK);
    }
    @PostMapping("/createExercise/")
    public ResponseEntity<ExerciseResponseDTO> createExercise(@RequestBody ExerciseCreateDTO exerciseCreateDTO){
        return new ResponseEntity<>(exerciseService.createExercise(exerciseCreateDTO), HttpStatus.CREATED);

    }



}
