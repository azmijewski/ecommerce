package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address-generator")
    @SequenceGenerator(name = "address-generator", sequenceName = "next_address_id")
    private Long id;
    private String name;
    private String street;
    private String city;
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
