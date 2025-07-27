package com._3.TurboFit.api.service;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.User;

import java.util.List;


public interface CarService {
    CarDTO createCar(CarDTO carDTO);
    List<CarDTO> getAll();
     CarDTO mapToDTO(Car car);

     CarDTO deleteCarByName(String carName);

     CarDTO updateCarHorsePower(Long userId);
}
