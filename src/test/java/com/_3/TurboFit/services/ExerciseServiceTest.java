package com._3.TurboFit.services;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.Exercise;
import com._3.TurboFit.api.models.Workout;
import com._3.TurboFit.api.repository.ExerciseRepository;
import com._3.TurboFit.api.repository.WorkoutRepository;
import com._3.TurboFit.api.service.serviceImpl.ExerciseServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {
    @Mock
    ExerciseRepository exerciseRepository;
    @Mock
    WorkoutRepository workoutRepository;
    @InjectMocks
    ExerciseServiceImpl exerciseService;

    private Exercise existingExercise;
    private Workout existingWorkout;
    private ExerciseResponseDTO dto;
    @BeforeEach
    void setUp() {
        existingExercise = new Exercise();
        existingExercise.setExerciseId(10L);
        existingExercise.setName("Squat");

        existingWorkout = new Workout();
        existingWorkout.setWorkoutId(5L);

        dto = new ExerciseResponseDTO();
        dto.setName("Squat");

    }
    @Test
    void createExercise_ShouldReturnExerciseResponseDTO(){
        ExerciseCreateDTO exerciseDtoIn = new ExerciseCreateDTO();
        exerciseDtoIn.setName("Bench Press");


        Exercise saved = new Exercise();
        saved.setExerciseId(2L);
        saved.setName("Bench Press");

        when(exerciseRepository.save(any())).thenReturn(saved);

        ExerciseResponseDTO exerciseDtoOut = exerciseService.createExercise(exerciseDtoIn);
        assertNotNull(exerciseDtoOut);
        assertEquals("Bench Press", exerciseDtoOut.getName());


    }

    @Test
    void deleteExercise_ShouldReturnDto(){
        Long id = 2L;
        Exercise exercise =  new Exercise();
        exercise.setExerciseId(id);
        exercise.setName("Bench Press");

        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));

        ExerciseResponseDTO dto = exerciseService.deleteExercise(id);

        verify(exerciseRepository, times(1)).delete(exercise);

        assertNotNull(dto);
        assertEquals(id, dto.getExerciseId());
        assertEquals("Bench Press", dto.getName());




    }

    @Test
    void getExercises_ShouldReturnList(){
        Exercise saved = new Exercise();
        saved.setExerciseId(1L);
        saved.setName("Bench Press");



        Exercise saved2 = new Exercise();
        saved2.setExerciseId(2L);
        saved2.setName("Squat");


        exerciseRepository.save(saved);
        exerciseRepository.save(saved2);

        when(exerciseRepository.findAll()).thenReturn(List.of(saved, saved2));

        List<ExerciseResponseDTO> dtos = exerciseService.getExercises();

        assertNotNull(dtos);
        Assertions.assertThat(dtos).hasSize(2);

        ExerciseResponseDTO bench = dtos.get(0);
        assertEquals(1L,bench.getExerciseId());
        assertEquals("Bench Press",bench.getName());

        ExerciseResponseDTO squat = dtos.get(1);
        assertEquals(2L,squat.getExerciseId());
        assertEquals("Squat",squat.getName());
    }

}
