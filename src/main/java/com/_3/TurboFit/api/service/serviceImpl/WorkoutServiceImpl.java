package com._3.TurboFit.api.service.serviceImpl;

import com._3.TurboFit.api.exception.ResourceNotFoundException;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.WorkoutDTO;
import com._3.TurboFit.api.models.Exercise;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.models.Workout;
import com._3.TurboFit.api.models.WorkoutExercise;
import com._3.TurboFit.api.repository.ExerciseRepository;
import com._3.TurboFit.api.repository.UserRepository;
import com._3.TurboFit.api.repository.WorkoutRepository;
import com._3.TurboFit.api.service.ExerciseService;
import com._3.TurboFit.api.service.WorkoutService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class WorkoutServiceImpl implements WorkoutService {


    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseService exerciseService;

    private final ExerciseRepository exerciseRepository;

    public WorkoutServiceImpl(UserRepository userRepository, WorkoutRepository workoutRepository, ExerciseService exerciseService, ExerciseRepository exerciseRepository) {
        this.userRepository = userRepository;
        this.workoutRepository = workoutRepository;
        this.exerciseService = exerciseService;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    @Transactional
    public WorkoutDTO startWorkout(Long userId, WorkoutDTO workoutDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: "+ userId));
        Workout workout = new Workout();
        workout.setWorkoutName(workoutDTO.getName());
        workout.setStartTime(LocalDateTime.now());
        workout.setUser(user);

        workout.setFinished(false);
        user.setTotalWorkouts(user.getTotalWorkouts()+1);


        workout.setWorkoutExercises(new ArrayList<>());

        workoutRepository.save(workout);
        userRepository.save(user);
    return mapToWorkoutDto(workout);
    }

    @Override
    public List<WorkoutDTO> getAll() {
        List<Workout> workouts = workoutRepository.findAll();
        return workouts.stream().map(workout -> mapToWorkoutDto(workout)).collect(Collectors.toList());
    }

    @Override
    public WorkoutDTO getWorkout(Long workoutId) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with Id: " + workoutId));
        return mapToWorkoutDto(workout);
    }


    @Override
    @Transactional
    public WorkoutDTO finishWorkout(Long workoutId, WorkoutDTO workoutDTO) {
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with Id: " + workoutId));

        if (workout.isFinished()) {
            throw new IllegalStateException("Workout already finished");
        }


        if (workoutDTO.getName() != null && !workoutDTO.getName().isBlank()) {
            workout.setWorkoutName(workoutDTO.getName());
        }

        workout.setFinished(true);
        workout.setEndTime(LocalDateTime.now());
        workout.getUser().setPoints(workout.getUser().getPoints() + 10);

        workoutRepository.save(workout);

        return mapToWorkoutDto(workout);
    }


    private WorkoutDTO mapToWorkoutDto(Workout workout) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setName(workout.getWorkoutName());
        workoutDTO.setWorkoutId(workout.getWorkoutId());
        workoutDTO.setStartTime(workout.getStartTime());
        workoutDTO.setEndTime(workout.getEndTime());

        workoutDTO.setFinished(workout.isFinished());

        workoutDTO.setExercises(workout.getWorkoutExercises().stream()
                .map(this::mapToExerciseDTO)
                .collect(Collectors.toList()));
        return workoutDTO;
    }
    private ExerciseResponseDTO mapToExerciseDTO(WorkoutExercise workoutExercise) {
        ExerciseResponseDTO dto = new ExerciseResponseDTO();
        dto.setExerciseId(workoutExercise.getExercise().getExerciseId());
        dto.setName(workoutExercise.getExercise().getName());
        dto.setMuscleGroup(workoutExercise.getExercise().getMuscleGroup());
        dto.setSets(workoutExercise.getSets());
        dto.setReps(workoutExercise.getReps());
        dto.setWeight(workoutExercise.getWeight());
        return dto;
    }
    private Exercise mapToEntity(ExerciseResponseDTO exerciseResponseDTO){
        Exercise exercise = new Exercise();
        exercise.setExerciseId(exerciseResponseDTO.getExerciseId());
        exercise.setName(exerciseResponseDTO.getName());

        return exercise;
    }
}
