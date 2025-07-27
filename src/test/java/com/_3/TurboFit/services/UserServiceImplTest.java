package com._3.TurboFit.services;

import com._3.TurboFit.api.dto.UserRegisterDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.repository.CarRepository;
import com._3.TurboFit.api.repository.UserRepository;
import com._3.TurboFit.api.service.serviceImpl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    CarRepository carRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;
    @Test
    void register_ShouldInvokeSaveAndReturnId() {
        UserRegisterDTO dto = new UserRegisterDTO(
                "Todor", "todo@example.com", "pass", "0888123456"
        );

        when(userRepository.findByUsername("Todor"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("pass"))
                .thenReturn("hashedPika");

        User saved = new User();
        saved.setUserId(100L);
        saved.setUsername("Todor");
        saved.setEmail("todo@example.com");
        saved.setPassword("pass");
        saved.setPhone("0888123456");
        saved.setRole("USER");
        when(userRepository.save(any(User.class)))
                .thenReturn(saved);

        UserResponseDTO response = userService.register(dto);

        verify(userRepository, times(1)).save(any(User.class));
        assertNotNull(response);
        assertEquals(100L, response.getUserId());
    }

    @Test
    void register_DuplicateUsername_ThrowsException(){
        UserRegisterDTO registerUser = new UserRegisterDTO("Todor", "todo@example.com", "pass", "0888123456");
        when(userRepository.findByUsername("Todor")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> userService.register(registerUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getAll_shouldReturnAllUsers(){
        User saved = new User();
        saved.setUserId(2L);
        saved.setUsername("Petur");
        saved.setEmail("petur@example.com");
        saved.setPassword("pass");
        saved.setPhone("083234456");
        saved.setRole("USER");

        User saved1 = new User();
        saved.setUserId(1L);
        saved.setUsername("Todor");
        saved.setEmail("todo@example.com");
        saved.setPassword("pass");
        saved.setPhone("0888123456");
        saved.setRole("USER");

        userRepository.save(saved);
        userRepository.save(saved1);

        when(userRepository.findAll()).thenReturn(List.of(saved, saved1));

        List<UserResponseDTO> dtos = userService.getAll();

        assertNotNull(dtos);
        Assertions.assertThat(dtos).hasSize(2);

    }

    @Test
    void selectCar_ShouldAttachCarToUserAndReturnDto() {
        Long userId = 2L;
        Long carId  = 1L;

        User user = new User();
        user.setUserId(userId);
        user.setUsername("Petur");
        user.setRole("USER");
        assertNull(user.getCar());

        Car car = new Car();
        car.setCarId(carId);
        car.setCarName("Ferrari");
        car.setHorsePower(225);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        when(carRepository.findById(carId))
                .thenReturn(Optional.of(car));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO dto = userService.selectCar(userId, carId);


        verify(userRepository, times(1)).findById(userId);
        verify(carRepository,  times(1)).findById(carId);
        verify(userRepository, times(1)).save(user);

        assertNotNull(user.getCar(), "Поставената car не трябва да е null");
        assertEquals(carId, user.getCar().getCarId());
        assertEquals("Ferrari", user.getCar().getCarName());

        assertEquals(userId, dto.getUserId());
        assertNotNull(dto.getCar());
        assertEquals(carId, dto.getCar().getCarId());
        assertEquals("Ferrari", dto.getCar().getCarName());
    }



}
