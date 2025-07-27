package com._3.TurboFit.services;

import com._3.TurboFit.api.models.User;
import com._3.TurboFit.api.repository.UserRepository;
import com._3.TurboFit.api.service.serviceImpl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    CustomUserDetailsService customUserDetails;

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {

        User user = new User();
        user.setUsername("Todor");
        user.setPassword("hashedPass");
        user.setRole("USER");


        when(userRepository.findByUsername("Todor"))
                .thenReturn(Optional.of(user));

        UserDetails ud = customUserDetails.loadUserByUsername("Todor");

        assertNotNull(ud, "UserDetails не трябва да е null");
        assertEquals("Todor", ud.getUsername());
        assertEquals("hashedPass", ud.getPassword());

        assertTrue(ud.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
        );
    }
    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                        customUserDetails.loadUserByUsername("missing"),
                "При липса на user трябва да се хвърли UsernameNotFoundException"
        );
    }
}
