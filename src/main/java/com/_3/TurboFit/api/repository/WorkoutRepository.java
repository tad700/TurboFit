package com._3.TurboFit.api.repository;

import com._3.TurboFit.api.models.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout,Long> {
}
