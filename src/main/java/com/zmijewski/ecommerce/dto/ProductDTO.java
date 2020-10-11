package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@Data
public class ProductDTO implements Serializable {
    @Null
    private Long id;
    @NotBlank
    private String name;
    @Min(0)
    private Integer quantity;
    private BigDecimal price;
    private boolean available;
    private String description;
    private String brand;
    @NotNull
    private Long brandId;
    @NotNull
    private Long categoryId;
    private List<Long> imagesIds;
    private Map<String, String> attributes = new HashMap<>();
}
