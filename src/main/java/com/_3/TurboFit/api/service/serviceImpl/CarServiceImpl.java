package com._3.TurboFit.api.service.serviceImpl;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.exception.ResourceNotFoundException;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.models.UserCar;
import com._3.TurboFit.api.repository.CarRepository;
import com._3.TurboFit.api.repository.UserRepository;
import com._3.TurboFit.api.service.CarService;
import com._3.TurboFit.api.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private CarRepository carRepository;
    private UserRepository userRepository;

    public CarServiceImpl(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CarDTO createCar(CarDTO carDTO) {
        Car car = mapToEntity(carDTO);
        Car savedCar = carRepository.save(car);
        return mapToDTO(savedCar);
    }

    @Override
    public List<CarDTO> getAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

    }

    private Car mapToEntity(CarDTO carDTO){
        Car car = new Car();
        car.setCarName(carDTO.getCarName());
        car.setHorsePower(carDTO.getHorsePower());
        car.setImageUrl(carDTO.getImageUrl());
        return car;
    }
    public CarDTO mapToDTO(Car car){
        CarDTO carDTO = new CarDTO();
        carDTO.setCarId(car.getCarId());
        carDTO.setCarName   (car.getCarName());
        carDTO.setHorsePower(car.getHorsePower());
        carDTO.setImageUrl(car.getImageUrl());
        return carDTO;
    }

    @Override
    public CarDTO deleteCarByName(String carName) {
        Car car = carRepository.findByCarName(carName).orElseThrow(() -> new ResourceNotFoundException("Car not found with name: "+carName));
        carRepository.delete(car);
       return mapToDTO(car);
    }

    @Override
    @Transactional
    public CarDTO updateCarHorsePower(Long userId) {
        int price = 10;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (user.getPoints() < price) {
            throw new IllegalArgumentException("Not enough points to upgrade horsepower");
        }

        UserCar userCar = user.getUserCar();
        if (userCar == null) {
            throw new ResourceNotFoundException("User does not have a selected car");
        }

        user.setPoints(user.getPoints() - price);
        userCar.setCustomHorsePower(userCar.getCustomHorsePower() + 10);

        userRepository.save(user);

        CarDTO dto = new CarDTO();
        dto.setCarId(userCar.getBaseCar().getCarId());
        dto.setCarName(userCar.getBaseCar().getCarName());
        dto.setImageUrl(userCar.getBaseCar().getImageUrl());
        dto.setHorsePower(userCar.getCustomHorsePower());
        return dto;
    }


}
