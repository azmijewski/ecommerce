package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CategoryDTO implements Serializable {
    private Long id;
    private String name;
}
