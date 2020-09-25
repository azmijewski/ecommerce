package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.jwt.AuthenticationRequest;
import com.zmijewski.ecommerce.dto.jwt.RefreshTokenRequest;
import com.zmijewski.ecommerce.dto.jwt.TokenResponse;
import com.zmijewski.ecommerce.service.AuthorizationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
    @PostMapping("token")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        log.info("Trying to authenticate user: {}", authenticationRequest.getEmail());
        TokenResponse tokenResponse = authorizationService.authenticateUser(authenticationRequest);
        return ResponseEntity.ok(tokenResponse);
    }
    @PostMapping("refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        log.info("Trying to refresh token: {}", refreshTokenRequest.getRefreshToken());
        TokenResponse tokenResponse = authorizationService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(tokenResponse);
    }
}
