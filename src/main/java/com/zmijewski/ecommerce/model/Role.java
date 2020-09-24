package com.zmijewski.ecommerce.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role-generator")
    @SequenceGenerator(name = "role-generator", sequenceName = "next_role_id")
    private Long id;
    @Field
    private String name;
    private String description;
}
