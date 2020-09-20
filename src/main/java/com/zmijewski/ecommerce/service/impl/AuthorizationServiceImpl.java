package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.configuration.jwt.JwtUtils;
import com.zmijewski.ecommerce.dto.jwt.AuthenticationRequest;
import com.zmijewski.ecommerce.dto.jwt.RefreshTokenRequest;
import com.zmijewski.ecommerce.dto.jwt.TokenResponse;
import com.zmijewski.ecommerce.exception.CustomAuthenticationException;
import com.zmijewski.ecommerce.service.AuthorizationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthorizationServiceImpl implements AuthorizationService {
    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthorizationServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }
    @Override
    public TokenResponse authenticateUser(AuthenticationRequest authenticationRequest) {
        Authentication auth;
        try {
            auth = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        } catch (Exception e) {
            log.error("Could not authenticate user with username: {}", authenticationRequest.getEmail());
            throw new CustomAuthenticationException(e.getMessage());
        }
        String accessToken = jwtUtils.generateAccessToken(auth.getName());
        String refreshToken = jwtUtils.generateRefreshToken(auth.getName());
        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiration(jwtUtils.getAccessTokenExpiration())
                .refreshToken(refreshToken)
                .refreshTokenExpiration(jwtUtils.getRefreshTokenExpiration())
                .tokenType(TOKEN_TYPE)
                .build();
    }
    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new CustomAuthenticationException("Could not refresh token");
        }
        String userName = jwtUtils.getUsernameFromToken(refreshToken);
        String accessToken = jwtUtils.generateAccessToken(userName);
        return TokenResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiration(jwtUtils.getAccessTokenExpiration())
                .build();
    }


    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
