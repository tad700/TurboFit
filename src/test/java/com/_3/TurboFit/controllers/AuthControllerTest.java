package com._3.TurboFit.controllers;

import com._3.TurboFit.api.controllers.AuthController;
import com._3.TurboFit.api.dto.UserLoginDTO;
import com._3.TurboFit.api.dto.UserRegisterDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ImportAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationManager authManager;

    @Test
    @DisplayName("POST /auth/register → 201 + UserResponseDTO JSON")

    void register_ShouldReturnCreatedUser() throws Exception {
        UserRegisterDTO in = new UserRegisterDTO();
        in.setUsername("alice");
        in.setEmail("alice@example.com");
        in.setPassword("secret");
        in.setPhone("0888000111");

        UserResponseDTO out = new UserResponseDTO();
        out.setUserId(10L);
        out.setUsername("alice");
        out.setEmail("alice@example.com");
        out.setPhone("0888000111");
        when(userService.register(any())).thenReturn(out);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.phone").value("0888000111"));
    }


    @Test
    @DisplayName("POST /auth/login → 401 + error message when bad credentials")
    void login_ShouldReturnUnauthorized_WhenBadCredentials() throws Exception {

        UserLoginDTO in = new UserLoginDTO();
        in.setUsername("bob");
        in.setPassword("wrong");

        doThrow(new BadCredentialsException("Bad creds"))
                .when(authManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(in)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
