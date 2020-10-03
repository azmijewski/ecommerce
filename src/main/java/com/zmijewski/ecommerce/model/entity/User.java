package com.zmijewski.ecommerce.model.entity;

import com.zmijewski.ecommerce.listeners.AuditListener;
import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.AuditObjectType;
import lombok.*;
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
@ToString(exclude = {"cart", "addresses", "orders", "role"})
@EntityListeners({AuditListener.class})
public class User implements Auditable {
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

    @Override
    @Transient
    public AuditObjectType getObjectType() {
        return AuditObjectType.USER;
    }
}
