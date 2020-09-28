package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class CategoryDTO implements Serializable {
    private Long id;
    @NotEmpty
    private String name;
}
