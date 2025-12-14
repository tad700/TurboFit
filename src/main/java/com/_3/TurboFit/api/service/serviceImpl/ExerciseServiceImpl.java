package com._3.TurboFit.api.service.serviceImpl;

import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.exception.ResourceNotFoundException;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.models.Exercise;
import com._3.TurboFit.api.models.Workout;
import com._3.TurboFit.api.models.WorkoutExercise;
import com._3.TurboFit.api.repository.ExerciseRepository;
import com._3.TurboFit.api.repository.WorkoutExerciseRepository;
import com._3.TurboFit.api.repository.WorkoutRepository;
import com._3.TurboFit.api.service.ExerciseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, WorkoutRepository workoutRepository, WorkoutExerciseRepository workoutExerciseRepository) {
        this.exerciseRepository = exerciseRepository;
        this.workoutRepository = workoutRepository;
        this.workoutExerciseRepository = workoutExerciseRepository;
    }

    @Override
    @Transactional
    public List<ExerciseResponseDTO> addToWorkout(Long workoutId, List<ExerciseResponseDTO> exerciseResponseDTOs) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Workout not found with id: " + workoutId));

        List<ExerciseResponseDTO> addedExercises = new ArrayList<>();

        for (ExerciseResponseDTO dto : exerciseResponseDTOs) {
            Exercise exercise = exerciseRepository.findByName(dto.getName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Exercise not found with name: " + dto.getName()));

            WorkoutExercise workoutExercise = new WorkoutExercise();
            workoutExercise.setExercise(exercise);
            workoutExercise.setWorkout(workout);
            workoutExercise.setSets(dto.getSets());
            workoutExercise.setReps(dto.getReps());
            workoutExercise.setWeight(dto.getWeight());

            workout.getWorkoutExercises().add(workoutExercise);
            workoutExerciseRepository.save(workoutExercise);

            addedExercises.add(mapToExerciseResponseDTO(workoutExercise));
        }

        return addedExercises;
    }



    @Override
    public List<ExerciseResponseDTO> getExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();
        return exercises.stream().map(exercise -> mapToExerciseResponseDTO(exercise)).collect(Collectors.toList());
    }

    @Override
    public ExerciseResponseDTO deleteExercise(Long id) {
        Exercise exerciseToDelete = exerciseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Exercise not forund with Id: "+ id));
        exerciseRepository.delete(exerciseToDelete);
        return mapToExerciseResponseDTO(exerciseToDelete);
    }

    @Override
    public ExerciseResponseDTO createExercise(ExerciseCreateDTO exerciseCreateDTO) {
        if(exerciseRepository.existsByName(exerciseCreateDTO.getName())){

            throw new RuntimeException("Exercise with that name already exists");
        }
            Exercise exercise = new Exercise();
            exercise.setName(exerciseCreateDTO.getName());
            exercise.setMuscleGroup(exerciseCreateDTO.getMuscleGroup());
            exerciseRepository.save(exercise);
            return mapToExerciseResponseDTO(exercise);


    }

    @Override
    public ExerciseResponseDTO deleteExerciseByName(String name) {
        Exercise exercise = exerciseRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException("Exercise not found with name: "+name));
        exerciseRepository.delete(exercise);
        return mapToExerciseResponseDTO(exercise);

    }


    private ExerciseResponseDTO mapToExerciseResponseDTO(Exercise exercise) {
        ExerciseResponseDTO exerciseResponseDTO = new ExerciseResponseDTO();
        exerciseResponseDTO.setExerciseId(exercise.getExerciseId());
        exerciseResponseDTO.setName(exercise.getName());
        exerciseResponseDTO.setMuscleGroup(exercise.getMuscleGroup());
        return exerciseResponseDTO;
    }

    public ExerciseResponseDTO mapToExerciseResponseDTO(WorkoutExercise workoutExercise) {
        ExerciseResponseDTO dto = new ExerciseResponseDTO();
        dto.setName(workoutExercise.getExercise().getName());
        dto.setSets(workoutExercise.getSets());
        dto.setReps(workoutExercise.getReps());
        dto.setWeight(workoutExercise.getWeight());
        return dto;
    }

}
