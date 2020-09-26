package com.zmijewski.ecommerce.model;

import com.zmijewski.ecommerce.enums.AuditLogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit-generator")
    @SequenceGenerator(name = "audit-generator", sequenceName = "next_audit_id")
    private Long id;
    private String message;
    @CreationTimestamp
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AuditLogType type;
}
