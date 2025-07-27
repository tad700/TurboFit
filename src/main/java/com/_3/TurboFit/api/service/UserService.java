package com._3.TurboFit.api.service;

import com._3.TurboFit.api.dto.*;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.models.UserCar;
import com._3.TurboFit.api.models.Workout;

import java.util.List;

public interface UserService {
    UserResponseDTO register(UserRegisterDTO userDto);
    List<UserResponseDTO> getAll();

    UserResponseDTO deleteUser(Long id);
    public UserResponseDTO selectCar(Long userId, Long carId);

    public UserResponseDTO mapToUserResponseDTO(User user);
    User findByUsername(String username);
    UserCar getUserCar(Long userId);

    UserResponseDTO findById(Long id);
    List<WorkoutDTO> getUserWorkouts(Long id);

    UserResponseDTO deleteUserByUsername(String username);
}
