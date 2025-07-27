package com._3.TurboFit.api.service.serviceImpl;

import com._3.TurboFit.api.exception.ResourceNotFoundException;
import com._3.TurboFit.api.dto.*;
import com._3.TurboFit.api.models.*;
import com._3.TurboFit.api.repository.CarRepository;
import com._3.TurboFit.api.repository.ExerciseRepository;
import com._3.TurboFit.api.service.CarService;
import com._3.TurboFit.api.service.UserService;
import com._3.TurboFit.api.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    //region Fields And Constructors
    private UserRepository userRepository;
    private CarRepository carRepository;
    private ExerciseRepository exerciseRepository;
    private CarService carService;
    PasswordEncoder passwordEncoder;
    @PostConstruct
    void createAdmin(){
        if(userRepository.findByUsername("ADMIN").isEmpty()){
            User admin = new User();
            admin.setUsername("ADMIN");
            admin.setPassword(passwordEncoder.encode("ADMIN"));
            admin.setEmail("admin@admin.com");
            admin.setRole("ADMIN");
            userRepository.save(admin);
        }


    }

    public UserServiceImpl(UserRepository userRepository, CarRepository carRepository, ExerciseRepository exerciseRepository, CarService carService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.exerciseRepository = exerciseRepository;
        this.carService = carService;
        this.passwordEncoder = passwordEncoder;
    }

    //endregion
    //region CRUD Methods
    @Override
    @Transactional
    public UserResponseDTO register(UserRegisterDTO dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }


        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRole("USER");

        User saved = userRepository.save(user);

        System.out.println("User registered "+saved.toString() );
        return mapToUserResponseDTO(saved);
    }
    @Override
    public List<UserResponseDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> mapToUserResponseDTO(user)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with Id: "+ id));
        userRepository.delete(user);
        return mapToUserResponseDTO(user);
    }


    @Override
    @Transactional
    public UserResponseDTO selectCar(Long userId, Long carId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Id: " + userId));

        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with Id: " + carId));


        UserCar userCar = new UserCar();
        userCar.setUser(user);
        userCar.setBaseCar(car);
        userCar.setCustomHorsePower(car.getHorsePower());

        user.setUserCar(userCar);

        userRepository.save(user);

        return mapToUserResponseDTO(user);
    }

    //endregion
    //region Mappers
    private User mapToEntity(UserDTO userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhone(userDto.getPhone());
        user.setTotalWorkouts(userDto.getTotalWorkouts());
        user.setRole(userDto.getRole());
        user.setPoints(userDto.getPoints());
        user.setUserCar(userDto.getCar() != null ? user.getUserCar() : null);



        return user;
    }
    private UserRegisterDTO mapToUserRegisterDTO(User user){

        UserRegisterDTO userDto = new UserRegisterDTO();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());

        return userDto;

    }
//endregion
public UserResponseDTO mapToUserResponseDTO(User user){
    UserResponseDTO userResponseDTO = new UserResponseDTO();
    userResponseDTO.setUserId(user.getUserId());
    userResponseDTO.setUsername(user.getUsername());

    userResponseDTO.setRole(user.getRole());
    userResponseDTO.setEmail(user.getEmail());
    userResponseDTO.setPhone(user.getPhone());
    userResponseDTO.setCar(user.getUserCar());
    userResponseDTO.setPoints(user.getPoints());
    userResponseDTO.setTotalWorkouts(user.getTotalWorkouts());
    return userResponseDTO;
}

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found with username "+username));
    }

    @Override
    public UserCar getUserCar(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));

        return user.getUserCar();

    }

    @Override
    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return mapToUserResponseDTO(user);
    }

    @Override
    public List<WorkoutDTO> getUserWorkouts(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        List<Workout> workouts = user.getWorkoutList();
        return workouts.stream().map(this::mapToWorkoutDto).collect(Collectors.toList());

        }

    @Override
    public UserResponseDTO deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found with username " + username));
        userRepository.delete(user);
        return mapToUserResponseDTO(user);
    }

    private WorkoutDTO mapToWorkoutDto(Workout workout) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setName(workout.getWorkoutName());
        workoutDTO.setWorkoutId(workout.getWorkoutId());
        workoutDTO.setStartTime(workout.getStartTime());
        workoutDTO.setEndTime(workout.getEndTime());

        workoutDTO.setFinished(workout.isFinished());

        workoutDTO.setExercises(workout.getWorkoutExercises().stream()
                .map(this::mapToExerciseDTO)
                .collect(Collectors.toList()));
        return workoutDTO;
    }
    private ExerciseResponseDTO mapToExerciseDTO(WorkoutExercise workoutExercise) {
        ExerciseResponseDTO dto = new ExerciseResponseDTO();
        dto.setExerciseId(workoutExercise.getExercise().getExerciseId());
        dto.setName(workoutExercise.getExercise().getName());
        dto.setMuscleGroup(workoutExercise.getExercise().getMuscleGroup());
        dto.setSets(workoutExercise.getSets());
        dto.setReps(workoutExercise.getReps());
        dto.setWeight(workoutExercise.getWeight());
        return dto;
    }
    private WorkoutDTO convertToWorkoutDTO(Workout workout) {
        WorkoutDTO workoutDTO = new WorkoutDTO();
        workoutDTO.setWorkoutId(workout.getWorkoutId());
        workoutDTO.setStartTime(workout.getStartTime());

        return workoutDTO;
    }


}
