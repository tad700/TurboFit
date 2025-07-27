package com._3.TurboFit.services;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.dto.WorkoutDTO;
import com._3.TurboFit.api.models.Exercise;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.models.Workout;
import com._3.TurboFit.api.repository.UserRepository;
import com._3.TurboFit.api.repository.WorkoutRepository;
import com._3.TurboFit.api.service.serviceImpl.WorkoutServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceImplTest {
    @InjectMocks
    WorkoutServiceImpl workoutService;
    @Mock
    WorkoutRepository workoutRepository;
    @Mock
    UserRepository userRepository;

    @Test
    void startWorkout_HappyPath() {

        User user = new User();
        user.setUserId(100L);
        user.setTotalWorkouts(0);
        user.setPoints(0);

        when(userRepository.findById(100L)).thenReturn(Optional.of(user));
        when(workoutRepository.save(any(Workout.class))).thenReturn(new Workout());
        when(userRepository.save(any(User.class))).thenReturn(user);


        WorkoutDTO dto = workoutService.startWorkout(100L, new WorkoutDTO());

        assertFalse(dto.isFinished());
        assertNotNull(dto.getStartTime());
        assertNull(dto.getEndTime());
        assertTrue(dto.getExercises().isEmpty());

        assertEquals(1, user.getTotalWorkouts());

        verify(userRepository).findById(100L);
        verify(workoutRepository).save(any());
        verify(userRepository).save(user);
    }
    @Test
    void getAll_ShouldReturnList(){
        User user1 = new User();
        user1.setUserId(100L);
        user1.setTotalWorkouts(0);
        user1.setPoints(0);
        User user2 = new User();
        user2.setUserId(101L);
        user2.setTotalWorkouts(0);
        user2.setPoints(0);

        Workout workout1 = new Workout(20L,"Morning",LocalDateTime.now(),null,false,new User(),new ArrayList<>());
        Workout workout2 = new Workout(21L,"Evening",LocalDateTime.now(),null,false,new User(),new ArrayList<>());
        workoutRepository.save(workout1);
        workoutRepository.save(workout2);

        when(workoutRepository.findAll()).thenReturn(List.of(workout1,workout2));
        List<WorkoutDTO> dtos = workoutService.getAll();
        assertNotNull(dtos);
        WorkoutDTO dto1 = dtos.get(0);
        assertEquals(20L,dto1.getWorkoutId());
        WorkoutDTO dto2 = dtos.get(1);
        assertEquals(21L,dto2.getWorkoutId());

    }
    @Test
    void getWorkout_ShouldReturnWorkout(){

        Workout workout1 = new Workout(20L,"Morning",LocalDateTime.now(),null,false,new User(),new ArrayList<>());
        when(workoutRepository.findById(20L))
                .thenReturn(Optional.of(workout1));
        WorkoutDTO dto = workoutService.getWorkout(20L);

        assertNotNull(dto);
        assertEquals(20L,dto.getWorkoutId());
        verify(workoutRepository).findById(20L);


    }
    @Test
    void finishWorkout_ShouldFinishWorkoutAndReturnDto() {

        User user1 = new User();
        user1.setUserId(100L);
        user1.setPoints(5);

        Workout workout1 = new Workout();
        workout1.setWorkoutId(20L);
        workout1.setUser(user1);
        workout1.setFinished(false);



        when(workoutRepository.findById(20L))
                .thenReturn(Optional.of(workout1));
        when(workoutRepository.save(any(Workout.class)))
                .thenReturn(workout1);
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setWorkoutId(20L);
        workoutDTO.setFinished(false);

        WorkoutDTO dto = workoutService.finishWorkout(20L,workoutDTO);


        assertNotNull(dto);
        assertEquals(20L, dto.getWorkoutId());
        assertTrue(workout1.isFinished());
        assertNotNull(workout1.getEndTime());


        assertEquals(15, user1.getPoints());
    }


}
