package com._3.TurboFit.controllers;


import com._3.TurboFit.api.controllers.AdminController;
import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.dto.ExerciseCreateDTO;
import com._3.TurboFit.api.dto.ExerciseResponseDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.Car;
import com._3.TurboFit.api.service.CarService;
import com._3.TurboFit.api.service.ExerciseService;
import com._3.TurboFit.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AdminController.class)

@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarService carService;
    @MockitoBean
    private ExerciseService exerciseService;
    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void addCar_shouldReturn201() throws Exception {
        CarDTO input = new CarDTO();
        input.setCarName("Tesla");
        input.setHorsePower(500);

        CarDTO output = new CarDTO();
        output.setCarId(1L);
        output.setCarName("Tesla");
        output.setHorsePower(500);

        when(carService.createCar(any())).thenReturn(output);

        mockMvc.perform(post("/admin/addCar/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.carName").value("Tesla"));

        verify(carService).createCar(any());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_usingMapper_shouldReturnFullDto() throws Exception {
        UserResponseDTO returned = new UserResponseDTO();
        returned.setUserId(42L);
        returned.setUsername("jane");
        returned.setEmail("jane@example.com");
        returned.setPhone("0888123456");
        returned.setRole("ADMIN");

        Car car = new Car();
        car.setCarId(5L);
        car.setCarName("Tesla");
        car.setHorsePower(700);
        returned.setCar(car);
        returned.setTotalWorkouts(7);
        returned.setPoints(150);

        when(userService.deleteUser(42L)).thenReturn(returned);


        String expectedJson = mapper.writeValueAsString(returned);


        mockMvc.perform(delete("/admin/deleteUser/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));

        verify(userService).deleteUser(42L);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void createExercise_usingMapper_shouldReturn201() throws Exception {

        ExerciseCreateDTO input = new ExerciseCreateDTO();
        input.setName("Squat");
        ExerciseResponseDTO output = new ExerciseResponseDTO();
        output.setExerciseId(7L);
        output.setName("Squat");

        when(exerciseService.createExercise(any())).thenReturn(output);


        mockMvc.perform(post("/admin/createExercise/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.exerciseId").value(7))
                .andExpect(jsonPath("$.name").value("Squat"));

        verify(exerciseService).createExercise(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_usingMapper_shouldReturnList() throws Exception {

        UserResponseDTO u1 = new UserResponseDTO();
        u1.setUserId(1L);
        u1.setUsername("alice");
        u1.setEmail("alice@example.com");
        UserResponseDTO u2 = new UserResponseDTO();
        u2.setUserId(2L);
        u2.setUsername("bob");
        u2.setEmail("bob@example.com");
        when(userService.getAll()).thenReturn(List.of(u1, u2));


        mockMvc.perform(get("/admin/getAllUsers"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(u1, u2))));

        verify(userService).getAll();
    }
}
