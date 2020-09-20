package com.zmijewski.ecommerce.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshTokenRequest implements Serializable {
    private String refreshToken;
}
