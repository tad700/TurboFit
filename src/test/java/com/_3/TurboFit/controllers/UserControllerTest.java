package com._3.TurboFit.controllers;

import com._3.TurboFit.api.controllers.UserController;
import com._3.TurboFit.api.dto.UserDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;


    @Test
    @WithMockUser
    @DisplayName("PUT /api/users/{userId}/select-car/{carId} → 200 + updated UserResponseDTO")
    void selectUserCar_ShouldReturnUpdatedUser() throws Exception {


        Long userId = 5L, carId = 10L;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(userId);
        dto.setUsername("todor");
        dto.setEmail("todor@example.com");
        dto.setPhone("0888000111");
        dto.setRole("USER");
        dto.setTotalWorkouts(3);
        dto.setPoints(30);

        when(userService.selectCar(userId, carId)).thenReturn(dto);


        mockMvc.perform(put("/api/users/{userId}/selectCar/{carId}", userId, carId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        verify(userService).selectCar(userId, carId);
    }
}
