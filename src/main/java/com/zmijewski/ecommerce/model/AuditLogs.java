package com.zmijewski.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuditLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit-generator")
    @SequenceGenerator(name = "audit-generator", sequenceName = "next_audit_id")
    private Long id;
    private String message;
    @CreationTimestamp
    private Date createdAt;
}
