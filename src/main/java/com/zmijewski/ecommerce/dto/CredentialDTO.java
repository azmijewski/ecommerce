package com.zmijewski.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class CredentialDTO implements Serializable {
    @NotBlank
    private String newPassword;
}
