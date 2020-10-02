package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class GlobalParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "parameter-generator", sequenceName = "next_parameter_id")
    private Long id;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private GlobalParameterName name;
    private String value;
}
