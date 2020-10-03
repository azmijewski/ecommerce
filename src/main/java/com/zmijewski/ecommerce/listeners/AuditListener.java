package com.zmijewski.ecommerce.listeners;

import com.zmijewski.ecommerce.model.Auditable;
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

    @PostPersist
    public void addPersistAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            auditLogService.createInfoAuditLog(auditable.getInsertMessage() + auditable.toString());
        }
    }

    @PostUpdate
    public void addUpdateAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            auditLogService.createInfoAuditLog(auditable.getUpdateMessage() + auditable.toString());
        }
    }

    @PostRemove
    public void addDeleteAuditLog(Object object) {
        if (object instanceof Auditable) {
            Auditable auditable = (Auditable) object;
            auditLogService.createInfoAuditLog(auditable.getDeleteMessage() + auditable.toString());
        }
    }


}
