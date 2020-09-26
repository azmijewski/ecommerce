package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.AuditLogDTO;
import com.zmijewski.ecommerce.enums.AuditLogSortType;
import org.springframework.data.domain.Page;

public interface AuditLogService {
    Page<AuditLogDTO> getAuditLogs(int page, int size, AuditLogSortType sortType);
    void createInfoAuditLog(String message);
    void createErrorAuditLog(String message);
}
