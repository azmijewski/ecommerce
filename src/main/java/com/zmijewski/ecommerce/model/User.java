package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-generator")
    @SequenceGenerator(name = "user-generator", sequenceName = "next_user_id")
    private Long id;
    @Email
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Cart> carts = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

}
