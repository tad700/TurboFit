package com._3.TurboFit.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Exercise")

public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long exerciseId;


    private String name;
    private String muscleGroup;




}

