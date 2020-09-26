package com.zmijewski.ecommerce.mapper;

import com.zmijewski.ecommerce.dto.AuditLogDTO;
import com.zmijewski.ecommerce.model.AuditLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {
    AuditLogDTO mapToDTO(AuditLog auditLog);
}
