package com.zmijewski.ecommerce.service;

import com.zmijewski.ecommerce.dto.AuditLogDTO;
import com.zmijewski.ecommerce.enums.AuditLogSortType;
import com.zmijewski.ecommerce.mapper.AuditLogMapper;
import com.zmijewski.ecommerce.model.AuditLog;
import com.zmijewski.ecommerce.repository.AuditLogRepository;
import com.zmijewski.ecommerce.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {
    @InjectMocks
    AuditLogServiceImpl auditLogService;
    @Mock
    AuditLogMapper auditLogMapper;
    @Mock
    AuditLogRepository auditLogRepository;

    @Test
    void shouldGetAuditLogs() {
        //given
        when(auditLogRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(new AuditLog()), PageRequest.of(0,10), 1));
        when(auditLogMapper.mapToDTO(any())).thenReturn(new AuditLogDTO());
        //when
        Page<AuditLogDTO> result = auditLogService.getAuditLogs(0, 10, AuditLogSortType.ID_ASC);
        //then
        assertFalse(result.isEmpty());
    }
    @Test
    void shouldCreateInfoAuditLog() {
        //given
        when(auditLogRepository.save(any())).thenReturn(new AuditLog());
        //when
        auditLogService.createInfoAuditLog("test");
        //then
        verify(auditLogRepository).save(any());
    }

    @Test
    void shouldCreateErrorAuditLog() {
        //given
        when(auditLogRepository.save(any())).thenReturn(new AuditLog());
        //when
        auditLogService.createErrorAuditLog("test");
        //then
        verify(auditLogRepository).save(any());
    }


}