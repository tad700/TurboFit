package com._3.TurboFit.api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkoutDTO {
    private Long workoutId;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<ExerciseResponseDTO> exercises;
    private boolean finished;


}
