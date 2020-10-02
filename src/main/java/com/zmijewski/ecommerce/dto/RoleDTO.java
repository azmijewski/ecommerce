package com.zmijewski.ecommerce.dto;

import org.hibernate.search.annotations.Field;

import javax.validation.constraints.NotBlank;

public class RoleDTO {
    private Long id;
    private String name;
    private String description;
}
