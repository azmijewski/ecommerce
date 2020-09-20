package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.configuration.jwt.JwtUtils;
import com.zmijewski.ecommerce.dto.jwt.AuthenticationRequest;
import com.zmijewski.ecommerce.dto.jwt.RefreshTokenRequest;
import com.zmijewski.ecommerce.dto.jwt.TokenResponse;
import com.zmijewski.ecommerce.exception.CustomAuthenticationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {
    @InjectMocks
    AuthorizationServiceImpl authorizationService;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtils jwtUtils;

    @Test
    void shouldAuthenticateUser() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@test");
        authenticationRequest.setPassword("test");
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken("test@test", "test");
        when(authenticationManager.authenticate(any())).thenReturn(authenticationToken);
        when(jwtUtils.generateAccessToken(any())).thenReturn("TEST ACCESS TOKEN");
        when(jwtUtils.generateRefreshToken(any())).thenReturn("TEST REFRESH TOKEN");
        when(jwtUtils.getAccessTokenExpiration()).thenReturn(1000L);
        when(jwtUtils.getRefreshTokenExpiration()).thenReturn(10000L);
        //when
        TokenResponse result = authorizationService.authenticateUser(authenticationRequest);
        //then
        assertNotNull(result.getAccessToken());
        assertNotNull(result.getRefreshToken());
    }
    @Test
    void shouldNotAuthenticate() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("test@test");
        authenticationRequest.setPassword("test");
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);
        //when && then
        assertThrows(CustomAuthenticationException.class, () -> authorizationService.authenticateUser(authenticationRequest));
    }
    @Test
    void shouldRefreshToken() {
        //given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("TEST REFRESH_TOKEN");
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(false);
        when(jwtUtils.getUsernameFromToken(anyString())).thenReturn("test@test");
        when(jwtUtils.generateAccessToken(any())).thenReturn("TEST ACCESS TOKEN");
        when(jwtUtils.getAccessTokenExpiration()).thenReturn(1000L);
        //when
        TokenResponse result = authorizationService.refreshToken(refreshTokenRequest);
        //then
        assertNotNull(result.getAccessToken());
    }
    @Test
    void shouldNotRefreshToken() {
        //given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("TEST REFRESH_TOKEN");
        when(jwtUtils.isTokenExpired(anyString())).thenReturn(true);
        //when && then
        assertThrows(CustomAuthenticationException.class, () -> authorizationService.refreshToken(refreshTokenRequest));
    }

}