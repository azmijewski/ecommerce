package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.model.entity.Role;
import com.zmijewski.ecommerce.model.entity.User;
import com.zmijewski.ecommerce.repository.UserRepository;
import com.zmijewski.ecommerce.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {
    @InjectMocks
    UserDetailsServiceImpl userDetailsService;
    @Mock
    UserRepository userRepository;

    @Test
    void shouldLoadByUsernameWithEmptyRoles() {
        //given
        User user = new User();
        user.setEmail("test@test");
        user.setPassword("test");
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(user));
        //when
        UserDetails result = userDetailsService.loadUserByUsername("test");
        //then
        assertTrue(result.getAuthorities().isEmpty());
    }
    @Test
    void shouldLoadByUsernameWithRoles() {
        //given
        User user = new User();
        user.setEmail("test@test");
        user.setPassword("test");
        Role role = new Role();
        role.setName("Test");
        user.setRole(role);
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.of(user));
        //when
        UserDetails result = userDetailsService.loadUserByUsername("test");
        //then
        assertFalse(result.getAuthorities().isEmpty());
    }
    @Test
    void shouldNotLoadByUsername() {
        //given
        when(userRepository.findActivatedUserByEmail(anyString())).thenReturn(Optional.empty());
        //when && then
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("test"));
    }
}