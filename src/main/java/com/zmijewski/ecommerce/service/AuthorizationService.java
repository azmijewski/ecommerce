package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.jwt.AuthenticationRequest;
import com.zmijewski.ecommerce.dto.jwt.RefreshTokenRequest;
import com.zmijewski.ecommerce.dto.jwt.TokenResponse;

public interface AuthorizationService {
    TokenResponse authenticateUser(AuthenticationRequest authenticationRequest);
    TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);

}
