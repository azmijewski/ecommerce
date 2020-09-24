package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class AddressDTO implements Serializable {
    private Long id;
    private String name;
    private String street;
    private String city;
    private String postalCode;
    private Long userId;
}
