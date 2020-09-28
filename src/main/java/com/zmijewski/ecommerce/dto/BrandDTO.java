package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class BrandDTO implements Serializable {
    private Long id;
    @NotBlank
    private String name;
}
