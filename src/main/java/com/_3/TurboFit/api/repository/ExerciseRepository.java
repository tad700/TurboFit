package com._3.TurboFit.api.repository;

import com._3.TurboFit.api.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    Optional<Exercise> findByName(String name);
    boolean existsByName(String  name);

}
