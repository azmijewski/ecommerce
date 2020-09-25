package com.zmijewski.ecommerce.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class SearchDTO implements Serializable {
    @NotBlank
    private String searchWords;
}
