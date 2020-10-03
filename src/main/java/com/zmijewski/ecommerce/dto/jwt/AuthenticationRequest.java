package com.zmijewski.ecommerce.dto.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class AuthenticationRequest implements Serializable {
    private String email;
    private String password;

}
