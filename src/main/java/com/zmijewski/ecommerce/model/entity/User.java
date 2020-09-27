package com.zmijewski.ecommerce.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Indexed
@NamedEntityGraphs({
        @NamedEntityGraph(name = "user with address", attributeNodes = {
                @NamedAttributeNode(value = "addresses")
        })
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user-generator")
    @SequenceGenerator(name = "user-generator", sequenceName = "next_user_id")
    private Long id;
    @Email
    @Field
    private String email;
    private String password;
    @Field
    private String firstName;
    @Field
    private String lastName;
    @Field
    private String phoneNumber;
    private Boolean isActive;
    private String token;
    @CreationTimestamp
    private Date createdAt;
    private Date tokenCreatedAt;
    @ManyToOne
    @JoinColumn(name = "role_id")
    @IndexedEmbedded
    private Role role;

    @OneToOne(mappedBy = "user")
    private Cart cart;
    @OneToMany(mappedBy = "user")
    @IndexedEmbedded
    private List<Address> addresses = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    @IndexedEmbedded
    private List<Order> orders = new ArrayList<>();

}
