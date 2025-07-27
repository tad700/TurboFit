package com._3.TurboFit.api.dto;

import lombok.Data;

@Data
public class ExerciseResponseDTO {
    Long exerciseId;
    private String name;
    private String muscleGroup;
    private int sets;
    private int reps;
    private double weight;
}
