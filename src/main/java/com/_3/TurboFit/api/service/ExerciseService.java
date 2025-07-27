package com._3.TurboFit.api.service;

import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.models.Exercise;

import java.util.List;


public interface ExerciseService {
    List<ExerciseResponseDTO> addToWorkout(Long workoutId, List<ExerciseResponseDTO> exerciseResponseDTOs);
    List<ExerciseResponseDTO> getExercises();
    ExerciseResponseDTO deleteExercise(Long id);
    ExerciseResponseDTO createExercise(ExerciseCreateDTO exerciseCreateDTO);

    ExerciseResponseDTO deleteExerciseByName(String name);



}
