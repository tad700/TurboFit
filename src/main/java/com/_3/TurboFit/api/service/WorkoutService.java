package com._3.TurboFit.api.service;

import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.WorkoutDTO;

import java.util.List;

public interface WorkoutService {

    WorkoutDTO startWorkout(Long UserId,WorkoutDTO workoutDTO);
    List<WorkoutDTO> getAll();

    WorkoutDTO getWorkout(Long workoutId);

    WorkoutDTO finishWorkout(Long workoutId, WorkoutDTO workoutDTO);
}
