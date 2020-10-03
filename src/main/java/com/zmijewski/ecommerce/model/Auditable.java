package com.zmijewski.ecommerce.model;

import com.zmijewski.ecommerce.model.enums.AuditObjectType;

public interface Auditable {
   AuditObjectType getObjectType();
}
