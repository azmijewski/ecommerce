package com.zmijewski.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailDTO implements Serializable {
    private String subject;
    private String content;
    private String sendTo;
}
