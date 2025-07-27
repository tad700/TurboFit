package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.UserDTO;
import com._3.TurboFit.api.dto.WorkoutDTO;
import com._3.TurboFit.api.service.ExerciseService;
import com._3.TurboFit.api.service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/workout")

public class WorkoutController {
    WorkoutService workoutService;
    ExerciseService exerciseService;

    public WorkoutController(WorkoutService workoutService, ExerciseService exerciseService) {
        this.workoutService = workoutService;
        this.exerciseService = exerciseService;
    }

    @PostMapping("start/{userId}")
    public ResponseEntity<WorkoutDTO> createWorkout(
            @PathVariable Long userId,
            @RequestBody WorkoutDTO workoutDTO){
        return new ResponseEntity<>(workoutService.startWorkout(userId,workoutDTO), HttpStatus.CREATED);

        }

    @GetMapping("/{workoutId}")
    public WorkoutDTO getWorkout(@PathVariable Long workoutId) {
        return workoutService.getWorkout(workoutId);

    }
    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAll(){
        return new ResponseEntity<>(workoutService.getAll(),HttpStatus.OK);
    }

    @PostMapping("{workoutId}/finish")
    public ResponseEntity<WorkoutDTO> finishWorkout(
            @PathVariable Long workoutId,
            @RequestBody WorkoutDTO workoutDTO) {
        WorkoutDTO result = workoutService.finishWorkout(workoutId, workoutDTO);
        return ResponseEntity.ok(result);
    }

}
