package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class BrandDTO implements Serializable {
    private Long id;
    private String name;
}
