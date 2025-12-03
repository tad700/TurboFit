package com._3.TurboFit.api.controllers;

import com._3.TurboFit.api.dto.UserLoginDTO;
import com._3.TurboFit.api.dto.UserRegisterDTO;
import com._3.TurboFit.api.dto.UserResponseDTO;
import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.service.UserService;
import com._3.TurboFit.api.service.serviceImpl.TokenService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    UserService userService;
    AuthenticationManager authManager;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    private final TokenService tokenService;

    public AuthController(UserService userService, AuthenticationManager authManager, TokenService tokenService) {
        this.userService = userService;
        this.authManager = authManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")

    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO dto){
        UserResponseDTO created = userService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PostMapping("/token")
    public String token(User user){
        LOG.debug("Token requested for user: ",user.getUsername());
        String token =tokenService.generateToken(user);
        LOG.debug("Token generated {}",token);
        return token;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO dto) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );


            User user = userService.findByUsername(dto.getUsername());
            String token = tokenService.generateToken(user);


            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("username", user.getUsername());
            response.put("token",token);
            response.put("car",user.getUserCar());
            System.out.println(response);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }
    }


}
