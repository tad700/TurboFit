package com._3.TurboFit.api.repository;

import com._3.TurboFit.api.models.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise,Long> {
}
