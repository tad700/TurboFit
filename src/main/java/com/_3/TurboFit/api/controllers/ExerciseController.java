package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")

public class ExerciseController {
    private final ExerciseService exerciseService;
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping("addToWorkout/{workoutId}")
    public ResponseEntity<List<ExerciseResponseDTO>> addExercisesToWorkout(
            @PathVariable Long workoutId,
            @RequestBody List<ExerciseResponseDTO> exercises) {
        List<ExerciseResponseDTO> result = exerciseService.addToWorkout(workoutId, exercises);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public List<ExerciseResponseDTO> getExerciseNames() {
        return exerciseService.getExercises();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ExerciseResponseDTO> deleteExercise(@PathVariable Long id){
        return new ResponseEntity<>(exerciseService.deleteExercise(id), HttpStatus.OK);
    }


}
