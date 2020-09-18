package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.AuditLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends PagingAndSortingRepository<AuditLog, Long> {
}
