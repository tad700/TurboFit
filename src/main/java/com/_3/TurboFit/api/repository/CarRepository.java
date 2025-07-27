package com._3.TurboFit.api.repository;

import com._3.TurboFit.api.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Long> {

    Optional<Car> findByCarName(String carName);
}
