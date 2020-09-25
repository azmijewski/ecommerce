package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Indexed
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address-generator")
    @SequenceGenerator(name = "address-generator", sequenceName = "next_address_id")
    private Long id;
    private String name;
    @Field
    private String street;
    @Field
    private String city;
    @Field
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
