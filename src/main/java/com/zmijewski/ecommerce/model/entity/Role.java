package com.zmijewski.ecommerce.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Indexed
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role-generator")
    @SequenceGenerator(name = "role-generator", sequenceName = "next_role_id")
    private Long id;
    @Field
    private String name;
    private String description;
}
