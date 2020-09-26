package com.zmijewski.ecommerce.controller;

import com.zmijewski.ecommerce.dto.AuditLogDTO;
import com.zmijewski.ecommerce.enums.AuditLogSortType;
import com.zmijewski.ecommerce.service.AuditLogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("api/v1/")
@Log4j2
public class AuditLogController {
    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    @GetMapping("logs")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogs(@RequestParam(name = "page") @Min(0) int page,
                                                          @RequestParam(name = "size") @Min(1) int size,
                                                          @RequestParam(name = "sort", defaultValue = "DATE_ASC") AuditLogSortType sortType) {
        log.info("Getting logs page: {}, size{}", page, size);
        Page<AuditLogDTO> result = auditLogService.getAuditLogs(page, size, sortType);
        return ResponseEntity.ok(result);
    }
}
