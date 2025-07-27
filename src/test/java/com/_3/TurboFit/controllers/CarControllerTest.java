package com._3.TurboFit.controllers;

import com._3.TurboFit.api.controllers.CarController;
import com._3.TurboFit.api.dto.CarDTO;
import com._3.TurboFit.api.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CarService carService;

    @Test
    @DisplayName("GET /api/cars → 200 + non-empty list")
    @WithMockUser
    void getAll_ShouldReturnList() throws Exception {
        CarDTO c1 = new CarDTO();
        c1.setCarId(1L);
        c1.setCarName("Audi");
        c1.setHorsePower(180);
        CarDTO c2 = new CarDTO();
        c2.setCarId(2L);
        c2.setCarName("BMW");
        c2.setHorsePower(200);

        when(carService.getAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(c1, c2))));
    }

    @Test
    @DisplayName("GET /api/cars → 200 + empty list")
    @WithMockUser
    void getAll_ShouldReturnEmptyList() throws Exception {
        when(carService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
