package com.zmijewski.ecommerce.service.impl;

import com.zmijewski.ecommerce.dto.AuditLogDTO;
import com.zmijewski.ecommerce.model.enums.AuditLogSortType;
import com.zmijewski.ecommerce.model.enums.AuditLogType;
import com.zmijewski.ecommerce.mapper.AuditLogMapper;
import com.zmijewski.ecommerce.model.entity.AuditLog;
import com.zmijewski.ecommerce.repository.AuditLogRepository;
import com.zmijewski.ecommerce.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
    }

    @Override
    public Page<AuditLogDTO> getAuditLogs(int page, int size, AuditLogSortType sortType) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortType.getSortType()), sortType.getField());
        Pageable pageable = PageRequest.of(page, size, sort);
        return auditLogRepository.findAll(pageable)
                .map(auditLogMapper::mapToDTO);
    }

    @Override
    public void createInfoAuditLog(String message) {
        createAuditLog(message, AuditLogType.INFO);
    }

    @Override
    public void createErrorAuditLog(String message) {
        createAuditLog(message, AuditLogType.ERROR);
    }

    private void createAuditLog(String message, AuditLogType auditLogType) {
        AuditLog auditLog = new AuditLog();
        auditLog.setMessage(message);
        auditLog.setType(auditLogType);
        auditLogRepository.save(auditLog);
    }
}
