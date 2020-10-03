package com.zmijewski.ecommerce.listeners;

import com.zmijewski.ecommerce.model.Auditable;
import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import com.zmijewski.ecommerce.repository.GlobalParameterRepository;
import com.zmijewski.ecommerce.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
public class AuditListener {

    @Autowired
    private AuditLogService auditLogService;
    @Autowired
    private GlobalParameterRepository globalParameterRepository;

    @PostPersist
    public void addPersistAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            GlobalParameterName parameterName = auditable.getObjectType().getGlobalParameterPersistMessage();
            String message = globalParameterRepository.getValueAsString(parameterName);
            auditLogService.createInfoAuditLog(message + auditable.toString());
        }
    }

    @PostUpdate
    public void addUpdateAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            GlobalParameterName parameterName = auditable.getObjectType().getGlobalParameterUpdateMessage();
            String message = globalParameterRepository.getValueAsString(parameterName);
            auditLogService.createInfoAuditLog(message + auditable.toString());
        }
    }

    @PostRemove
    public void addDeleteAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            GlobalParameterName parameterName = auditable.getObjectType().getGlobalParameterDeleteMessage();
            String message = globalParameterRepository.getValueAsString(parameterName);
            auditLogService.createInfoAuditLog(message + auditable.toString());
        }
    }


}
