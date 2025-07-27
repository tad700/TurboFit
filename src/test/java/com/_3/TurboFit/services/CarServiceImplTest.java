package com._3.TurboFit.services;

import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.repository.CarRepository;
import com._3.TurboFit.api.service.serviceImpl.CarServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceImplTest {
    @Mock
    CarRepository carRepository;
    @InjectMocks
    CarServiceImpl carService;

    @Test
    void whenCreateCar_givenValidDto_thenReturnsDtoWithId() {
        CarDTO dtoIn = new CarDTO();
        dtoIn.setCarName("Audi");
        dtoIn.setHorsePower(200);

        Car saved = new Car();
        saved.setCarId(5L);
        saved.setCarName("Audi");
        saved.setHorsePower(200);
        when(carRepository.save(any())).thenReturn(saved);


        CarDTO dtoOut = carService.createCar(dtoIn);


        assertEquals(5L, dtoOut.getCarId());
        assertEquals("Audi", dtoOut.getCarName());
        assertEquals(200, dtoOut.getHorsePower());

    }

    @Test
    void getAll_shouldReturnList(){
        Long id = 1L;
        Car car = new Car();
        car.setCarId(id);
        car.setCarName("Ferrari");
        car.setHorsePower(250);

        Long id2 = 2L;
        Car car2 = new Car();
        car2.setCarId(id2);
        car2.setCarName("BMW");
        car2.setHorsePower(200);
        carRepository.save(car);
        carRepository.save(car2);

        when(carRepository.findAll()).thenReturn(List.of(car, car2));
        List<CarDTO> result = carService.getAll();

        assertNotNull(result);
        Assertions.assertThat(result).hasSize(2);
        CarDTO ferrari = result.get(0);
        assertEquals(1L,ferrari.getCarId());
        assertEquals("Ferrari",ferrari.getCarName());

        CarDTO bmw = result.get(1);
        assertEquals(2L,bmw.getCarId());
        assertEquals("BMW",bmw.getCarName());

    }
    
}
